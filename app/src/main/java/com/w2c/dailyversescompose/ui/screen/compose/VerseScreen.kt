package com.w2c.dailyversescompose.ui.screen.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.w2c.dailyversescompose.model.BottomSheetDialogSelection
import com.w2c.dailyversescompose.repo.VersesRepository
import com.w2c.dailyversescompose.ui.screen.loadImages
import com.w2c.dailyversescompose.ui.screen.shareVerseIntent
import com.w2c.dailyversescompose.viewmodel.VersesViewModel
import com.w2c.dailyversescompose.viewmodel.factory.WPViewModelFactory

@Composable
fun VerseScreen(viewModel: VersesViewModel) {
    var selectedVerse by remember { mutableStateOf("") }
    var openWallpaper by remember { mutableStateOf(false) }
    var shareVerse by remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf(false) }
    var wallpapers by remember { mutableStateOf(emptyList<String>()) }

    if (!openWallpaper) {
        Column {
            repeat(2) {
                TodayVerse(viewModel)
                Books(viewModel) { verse ->
                    selectedVerse = verse
                    dialogState = true
                }
            }
        }
    } else {
        val ctx = LocalContext.current as ComponentActivity
        loadImages(
            viewModel(factory = WPViewModelFactory(VersesRepository(ctx))),
            ctx
        ) { images ->
            wallpapers = images
        }
    }

    if (dialogState) {
        BottomSheetCompose { state, action ->
            if (action == BottomSheetDialogSelection.WALLPAPER) {
                openWallpaper = true
            } else if (action == BottomSheetDialogSelection.SHARE) {
                shareVerse = true
            }
            dialogState = state
        }
    }

    if (openWallpaper) {
        WallPaperList(selectedVerse, wallpapers) {
            openWallpaper = false
        }
    }

    if (shareVerse) {
        shareVerseIntent(LocalContext.current, selectedVerse)
        shareVerse = false
    }
}