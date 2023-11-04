package com.w2c.dailyversescompose.repo

import android.content.Context
import android.util.Log
import com.w2c.dailyversescompose.model.TodayVerse
import com.w2c.dailyversescompose.util.Util
import com.w2c.dailyversescompose.retrofit.Verses
import com.w2c.dailyversescompose.model.VersesResponse
import com.w2c.dailyversescompose.model.WallpaperResponse
import com.w2c.dailyversescompose.retrofit.NetworkHelper
import com.w2c.dailyversescompose.room.DBHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class VersesRepository(private val context: Context) {
    private var todayBookNo: Int = 0
    private var bookList = mutableListOf<String>()

    init {
        todayBookNo = Random.nextInt(56)
        bookList.addAll(Util.readBook(context))
    }

    suspend fun getVersesByBook(book: String): VersesResponse {
        val request: Verses = NetworkHelper.getVerses()
        return request.getChapterByBook(book.replace(" ", "") + Util.EXTENSION)
    }

    suspend fun getWallpapers(): WallpaperResponse {
        val request: Verses = NetworkHelper.getVerses()
        return request.getWallpapers()
    }

    private suspend fun insertVerse(dailyVerse: TodayVerse): Long {
        return withContext(Dispatchers.IO) {
            return@withContext DBHandler.getDatabase(context = context).verseDAO()
                .insert(dailyVerse)
        }
    }

    suspend fun getTodayVerse(todayDate: String): TodayVerse? {
        return withContext(Dispatchers.IO) {
            var todayVerse =
                DBHandler.getDatabase(context = context).verseDAO().getVerseByDate(todayDate)
            if (todayVerse == null) {
                todayVerse = getTodayVerseFromRemote(todayDate, getTodayBook())
            }
            return@withContext todayVerse
        }
    }

    private suspend fun getTodayVerseFromRemote(
        date: String,
        todayBook: String
    ): TodayVerse? {
        val response = getVersesByBook(todayBook)
        val todayVerse = filterTodayVerse(response)
        val verseWithDate = todayVerse.let {
            it.date = date
            it
        }
        return insertTodayVerse(dailyVerse = verseWithDate)
    }

    private fun filterTodayVerse(response: VersesResponse): TodayVerse {
        //Getting random chapter from response
        val chapters = response.chapters
        val chaptersSize = chapters.size
        val randomChapter = chapters[Random.nextInt(chaptersSize - 1)]

        //Getting random verse from randomly picked chapter
        val verses = randomChapter.verses
        val versesSize = verses.size
        val randomVerse = verses[Random.nextInt(versesSize - 1)]

        return TodayVerse(
            verse = randomVerse.text,
            verseNo = randomVerse.verse,
            chapterNo = randomChapter.chapter,
            bookName = getTodayBook()
        )
    }

    private suspend fun insertTodayVerse(dailyVerse: TodayVerse?): TodayVerse? {
        try {
            dailyVerse?.let {
                val insert =
                    insertVerse(dailyVerse = dailyVerse)
                Log.d("Insert Result", insert.toString())
                return dailyVerse
            }
        } catch (ex: Exception) {
            println(ex.stackTrace.toString())
        }
        return null
    }

    private fun getTodayBook() = bookList[todayBookNo]
    fun getBooks() = bookList
}