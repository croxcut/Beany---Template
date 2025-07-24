package com.example.core.composables

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest

@Composable
fun GifLoader(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(resId)
            .build(),
        imageLoader = imageLoader,
        contentDescription = "GIF"
    )
}