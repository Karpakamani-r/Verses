package com.w2c.dailyversescompose.ui.screen.compose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.w2c.dailyversescompose.model.Chapters
import com.w2c.dailyversescompose.model.Verse
import com.w2c.dailyversescompose.ui.theme.LightBlue
import com.w2c.dailyversescompose.ui.theme.LightPurple

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChapterRow(
    chapter: Chapters,
    verseSelection: (verse: String) -> Unit
) {
    val expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    Column(modifier = Modifier
        .padding(all = 16.dp)
        .fillMaxWidth()
        .background(LightPurple)
        .clickable {
            expanded.value = !expanded.value
        }) {
        Text(
            text = "Chapter ${chapter.chapter}",
            fontSize = 16.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier.padding(10.dp)
        )
        AnimatedVisibility(visible = expanded.value) {
            VersesRow(chapter = chapter.chapter, verses = chapter.verses, verseSelection)
        }
    }
}

@Composable
fun VersesRow(
    chapter: String,
    verses: List<Verse>,
    verseSelection: (verse: String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
            .background(LightBlue)
    ) {

        verses.forEach { verse ->
            Text(
                text = "Verse ${verse.verse} : ${verse.text}",
                fontSize = 16.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        verseSelection("$chapter : ${verse.verse}\n\n${verse.text}")
                    }
            )
        }
    }
}


