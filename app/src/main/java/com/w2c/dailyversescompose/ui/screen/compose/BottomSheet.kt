package com.w2c.dailyversescompose.ui.screen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.w2c.dailyversescompose.model.BottomSheetDialogSelection
import com.w2c.dailyversescompose.ui.theme.Transparent

@Composable
fun BottomSheetCompose(
    function: (dialogState: Boolean, action: BottomSheetDialogSelection) -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Column(
            modifier = Modifier
                .background(Transparent)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier.background(color = MaterialTheme.colors.background)
            ) {
                Text(
                    text = "Share", modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable {
                            function(false, BottomSheetDialogSelection.SHARE)
                        }
                )
                Text(
                    text = "Wallpaper", modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable {
                            function(false, BottomSheetDialogSelection.WALLPAPER)
                        }
                )
                Text(text = "Close", modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable {
                        function(false, BottomSheetDialogSelection.CLOSE)
                    })
            }
        }
    }
}


