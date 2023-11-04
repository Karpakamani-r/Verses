package com.w2c.dailyversescompose.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.w2c.dailyversescompose.repo.VersesRepository
import com.w2c.dailyversescompose.viewmodel.VersesViewModel
import com.w2c.dailyversescompose.viewmodel.WallpaperViewModel

@Suppress("UNCHECKED_CAST")
class WPViewModelFactory constructor(private val repository: VersesRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        WallpaperViewModel(repository) as T
}