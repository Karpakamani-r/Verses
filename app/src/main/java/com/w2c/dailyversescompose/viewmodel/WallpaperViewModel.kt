package com.w2c.dailyversescompose.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.w2c.dailyversescompose.model.WallpaperResponse
import com.w2c.dailyversescompose.repo.VersesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class WallpaperViewModel(private val repository: VersesRepository) : ViewModel() {

    val wallpaperLiveDate:MutableLiveData<WallpaperResponse> = MutableLiveData()

    fun getWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
           val wpResponse = repository.getWallpapers()
            wallpaperLiveDate.postValue(wpResponse)
        }
    }

}