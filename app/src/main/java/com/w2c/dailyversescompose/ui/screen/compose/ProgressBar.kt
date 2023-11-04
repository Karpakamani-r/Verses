package com.w2c.dailyversescompose.ui.screen.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        CircularProgressIndicator(modifier = modifier
            .align(alignment = Alignment.Center)
            .height(25.dp)
            .width(25.dp))
    }
}

@Preview
@Composable
fun PBR(){
    Column(modifier = Modifier.fillMaxWidth()) {
        ProgressBar()
    }
}