package com.w2c.dailyversescompose.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.w2c.dailyversescompose.model.Chapters
import com.w2c.dailyversescompose.model.TodayVerse
import com.w2c.dailyversescompose.model.Verse
import com.w2c.dailyversescompose.model.VersesResponse
import com.w2c.dailyversescompose.repo.VersesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class VersesViewModel(
    private val repository: VersesRepository
) :
    ViewModel() {

    private lateinit var randomVerse: Verse
    private lateinit var randomChapter: Chapters
    val versesLiveData = MutableLiveData<TodayVerse>()
    val chaptersLiveData = MutableLiveData<VersesResponse>()
    val todayVerseErrorResponse = MutableLiveData<String>()
    val chapterByVerseErrorResponse = MutableLiveData<String>()

    fun getTodayVerse(date: String) {
        viewModelScope.launch {
            try {
                val todayLocalVerse = repository.getTodayVerse(todayDate = date)
                versesLiveData.postValue(todayLocalVerse)
            } catch (exception: Exception) {
                todayVerseErrorResponse.postValue(getErrorMsg())
            }
        }
    }

    fun getChaptersByBook(todayBook: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getVersesByBook(todayBook)
                chaptersLiveData.postValue(response)
            } catch (exception: Exception) {
                chapterByVerseErrorResponse.postValue(getErrorMsg())
            }
        }
    }

    private fun getErrorMsg(): String {
        var errorMsg = "Failed to load Book"
        if (::randomChapter.isInitialized)
            errorMsg += " ${randomChapter.chapter}"
        if (::randomVerse.isInitialized)
            errorMsg += " ${randomVerse.verse}"
        return errorMsg
    }

    fun getBooks(): MutableList<String> {
       return repository.getBooks()
    }
}