package com.w2c.dailyversescompose.ui.screen.compose

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.w2c.dailyversescompose.R
import com.w2c.dailyversescompose.util.Util

@Composable
fun WallPaperList(verse: String, wallpapers: List<String>, close: () -> Unit) {
    if (wallpapers.isNotEmpty()) {

        val imageUrl =
            rememberSaveable { mutableStateOf(Util.BASE_URL + wallpapers.first()) }
        val selectedImage = rememberAsyncImagePainter(model = imageUrl.value)
        val offsetX = rememberSaveable { mutableStateOf(0f) }
        val offsetY = rememberSaveable { mutableStateOf(0f) }
        val fullScreenImage = rememberSaveable { mutableStateOf(false) }

        val modifier = Modifier.fillMaxSize()

        ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
            val (bigImage, bottomBanner) = createRefs()
            Box(modifier = modifier.constrainAs(bigImage) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(bottomBanner.top)
            }) {
                BigImageView(
                    modifier = modifier, painter = selectedImage,
                    close = close, isFullScreen = fullScreenImage.value
                ) { fullscreen ->
                    fullScreenImage.value = fullscreen
                }
                Text(
                    text = verse,
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .padding(16.dp)
                        .offset { IntOffset(offsetX.value.toInt(), offsetY.value.toInt()) }
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                offsetX.value += delta
                            }
                        )
                        .draggable(orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                offsetY.value += delta
                            }
                        )
                )
                Image(
                    painter = painterResource(id = if (fullScreenImage.value) R.drawable.ic_baseline_close else R.drawable.ic_baseline_zoom_out),
                    contentDescription = "Image Zoom",
                    modifier = Modifier
                        .clickable {
                            fullScreenImage.value = !fullScreenImage.value
                        }
                        .align(alignment = Alignment.BottomEnd)
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            bottom = if (fullScreenImage.value) 16.dp else 76.dp,
                            end = 16.dp
                        )
                )
            }
            if (!fullScreenImage.value) {
                BottomImageSlider(modifier = Modifier.constrainAs(bottomBanner) {
                    bottom.linkTo(parent.bottom)
                }, wallpapers = wallpapers) { url ->
                    imageUrl.value = Util.BASE_URL + url
                }
            }
        }
    }
}

@Composable
fun BottomImageSlider(
    modifier: Modifier,
    wallpapers: List<String>,
    onImageClick: (url: String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.horizontalScroll(rememberScrollState())
    )
    {
        val listRowModifier = Modifier
            .size(120.dp)
        wallpapers.forEach { image ->
            PhotoItem(image, listRowModifier) {
                onImageClick(image)
            }
        }
    }
}

@Composable
fun PhotoItem(
    imageUrl: String,
    modifier: Modifier,
    onImageClick: (painter: AsyncImagePainter) -> Unit
) {
    val ctx = LocalContext.current as ComponentActivity
    val model = rememberAsyncImagePainter(
        ImageRequest.Builder(ctx)
            .data(Util.BASE_URL + imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )
    Image(
        painter = model,
        contentScale = ContentScale.Crop,
        contentDescription = "Wallpaper Image",
        modifier = modifier.clickable {
            onImageClick(model)
        }
    )
}

@Composable
fun BigImageView(
    modifier: Modifier,
    isFullScreen: Boolean,
    painter: AsyncImagePainter,
    close: () -> Unit,
    fullScreenImage: (isFullScreen: Boolean) -> Unit
) {
    Image(
        painter = painter,
        contentDescription = "Full Screen Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
    BackHandler {
        if (isFullScreen) {
            fullScreenImage(false)
        } else {
            close()
        }
    }
}
