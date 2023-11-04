package com.w2c.dailyversescompose.util

import android.content.Context
import android.icu.text.SimpleDateFormat
import com.google.gson.Gson
import com.w2c.dailyversescompose.model.TodayVerse
import java.io.FileNotFoundException
import java.time.Duration
import java.util.*

object Util {
    const val TAG = "DailyVerses"
    const val SUCCESS = "Success $TAG"
    const val FAILURE = "Failure $TAG"
    const val BASE_URL =
        "https://verses66.000webhostapp.com/" //I am using Priya's Google login for 000webhostapp.com
    const val EXTENSION = ".json"
    const val VERSE_LOADING_MSG = "The Verse for the day is Loading..."

    fun readBook(context: Context): List<String> {
        return try {
            var res = ""
            context.resources.assets.open("books.json").bufferedReader().use {
                res += it.readLine()
            }
            val array = Gson().fromJson(res, Array<String>::class.java)
            val list = mutableListOf<String>()
            list.addAll(array)
            list
        } catch (fileNotFoundException: FileNotFoundException) {
            print(TAG + fileNotFoundException.message)
            emptyList()
        } catch (exception: Exception) {
            print(TAG + exception.message)
            emptyList()
        }
    }

    fun getTodayDate(): String {
        val today = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormatter.format(today)
    }

    fun getDifferentMillsToNextDay(): Long {
        val currentTime = Calendar.getInstance().time
        val now = Calendar.getInstance(Locale.getDefault())
        now.time = currentTime
        return getTargetTime().timeInMillis - now.timeInMillis
    }

    private fun getTargetTime(): Calendar {
        val calender = Calendar.getInstance(Locale.getDefault())
        calender.time = Date()
        calender.set(Calendar.HOUR_OF_DAY, 23)
        calender.set(Calendar.MINUTE, 59)
        calender.set(Calendar.SECOND, 59)
        return calender
    }

    fun formatVerse(todayVerse: TodayVerse?): String {
        return "${todayVerse?.bookName} ${todayVerse?.chapterNo} : ${todayVerse?.verseNo}\n\n${todayVerse?.verse}"
    }
}