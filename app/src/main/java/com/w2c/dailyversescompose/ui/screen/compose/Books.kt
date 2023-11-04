package com.w2c.dailyversescompose.ui.screen.compose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.w2c.dailyversescompose.R
import com.w2c.dailyversescompose.viewmodel.VersesViewModel

@Composable
fun Books(viewModel: VersesViewModel, onVerseSelect: (verse: String) -> Unit) {
    val books = viewModel.getBooks()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        books.forEach { book ->
            BookRow(book = book, viewModel) { verse ->
                onVerseSelect("$book $verse")
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BookRow(
    book: String,
    viewModel: VersesViewModel,
    verseSelection: (verse: String) -> Unit
) {
    val expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(all = 10.dp)
            .fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier
            .clickable {
                expanded.value = !expanded.value
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 13.dp)
            ) {
                Text(
                    text = book,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = if (expanded.value) R.drawable.ic_keyboard_arrow_up else R.drawable.ic_keyboard_arrow_down),
                    contentDescription = "Arrow Image"
                )
            }
            AnimatedVisibility(visible = expanded.value) {
                Chapters(book, viewModel, verseSelection)
            }
        }
    }
}