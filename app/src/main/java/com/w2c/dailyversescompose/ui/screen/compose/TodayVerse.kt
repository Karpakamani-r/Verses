package com.w2c.dailyversescompose.ui.screen.compose

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.w2c.dailyversescompose.model.Chapters
import com.w2c.dailyversescompose.ui.screen.addObserver
import com.w2c.dailyversescompose.util.Util
import com.w2c.dailyversescompose.util.setAsWallpaper
import com.w2c.dailyversescompose.viewmodel.VersesViewModel

@Composable
fun TodayVerse(viewModel: VersesViewModel) {
    val verseOfTheDay: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val verseTitleOfTheDay: MutableState<String> =
        rememberSaveable { mutableStateOf(Util.VERSE_LOADING_MSG) }

    val canShowMore = remember { mutableStateOf(false) }
    val isMore = remember { mutableStateOf(false) }
    val context = LocalContext.current as ComponentActivity

    addObserver(context = context, viewModel = viewModel) { title: String, verse: String ->
        verseTitleOfTheDay.value = title
        verseOfTheDay.value = verse
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = verseTitleOfTheDay.value, fontWeight = FontWeight.Bold)
        Text(
            text = verseOfTheDay.value,
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable {
                    setAsWallpaper(context, verseOfTheDay.value)
                },
            onTextLayout = {
                if (it.hasVisualOverflow)
                    canShowMore.value = true
            },
            maxLines = if (isMore.value) Int.MAX_VALUE else 3,
            overflow = if (isMore.value) TextOverflow.Visible else TextOverflow.Ellipsis
        )

        if (canShowMore.value) {
            Text(
                text = if (isMore.value) "Less" else "More", modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clickable { isMore.value = !isMore.value },
                fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Chapters(
    book: String,
    viewModel: VersesViewModel,
    verseSelection: (verse: String) -> Unit
) {
    val chapters: MutableState<List<Chapters>> = rememberSaveable { mutableStateOf(emptyList()) }
    val dataLoaded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    val ctx = LocalContext.current as ComponentActivity

    //Observing chapters update
    viewModel.chaptersLiveData.observe(ctx) {
        chapters.value = it.chapters
        dataLoaded.value = true
    }

    viewModel.chapterByVerseErrorResponse.observe(ctx) {
        Toast.makeText(ctx, it, Toast.LENGTH_LONG).show()
    }

    //Displaying Progressbar
    if (!dataLoaded.value) {
        ProgressBar()
    }

    //Creating chapter list
    Column {
        chapters.value.forEach { chapter ->
            ChapterRow(chapter, verseSelection)
        }
    }

    //API call to fetch chapters from Backend
    viewModel.getChaptersByBook(book)
}