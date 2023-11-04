package com.w2c.dailyversescompose.ui.screen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.w2c.dailyversescompose.repo.VersesRepository
import com.w2c.dailyversescompose.ui.screen.compose.VerseScreen
import com.w2c.dailyversescompose.ui.theme.DailyVersesComposeTheme
import com.w2c.dailyversescompose.util.Util
import com.w2c.dailyversescompose.viewmodel.VersesViewModel
import com.w2c.dailyversescompose.viewmodel.WallpaperViewModel
import com.w2c.dailyversescompose.viewmodel.factory.ViewModelFactory
import com.w2c.dailyversescompose.work.VerseNotificationWork

class MainActivity : ComponentActivity() {
    lateinit var viewModel: VersesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repo = VersesRepository(LocalContext.current)
            viewModel = viewModel(factory = ViewModelFactory(this, repo))
            DailyVersesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    VerseScreen(viewModel = viewModel)
                }
            }
            viewModel.getTodayVerse(Util.getTodayDate())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkNotificationPermission()
            } else {
                VerseNotificationWork.scheduleWork(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkNotificationPermission() {
        val permissions = Array(1) { "android.permission.POST_NOTIFICATIONS" }
        when {
            ContextCompat.checkSelfPermission(
                this,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED -> {
                VerseNotificationWork.scheduleWork(this)
            }
            shouldShowRequestPermissionRationale(permissions[0]) -> {
                Toast.makeText(
                    this,
                    "Please enable notification permission in setting",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                requestPermissions(
                    permissions,
                    10
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            10 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    VerseNotificationWork.scheduleWork(this)
                } else {
                    Toast.makeText(
                        this,
                        "Notification permission is requires to send daily verse!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

fun shareVerseIntent(context: Context, selectedVerse: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.type = "text/plain"
    sendIntent.putExtra(Intent.EXTRA_TEXT, selectedVerse)
    context.startActivity(Intent.createChooser(sendIntent, "Share Verse"))
}

fun addObserver(context: Context, viewModel: VersesViewModel, function: (String, String) -> Unit) {
    val ctx = context as ComponentActivity
    viewModel.versesLiveData.observe(ctx) {
        function("${it.bookName} ${it.chapterNo} : ${it.verseNo}", it.verse)
    }
    viewModel.todayVerseErrorResponse.observe(ctx) {
        function(it, Util.FAILURE)
    }
}

fun loadImages(
    wallpaperViewModel: WallpaperViewModel,
    ctx: ComponentActivity,
    function: (images: List<String>) -> Unit
) {
    wallpaperViewModel.wallpaperLiveDate.observe(ctx) { response ->
        function(response.images)
    }
    wallpaperViewModel.getWallpapers()
}