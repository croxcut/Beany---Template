package com.example.core.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun LogoCard(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int
) {
    Image(
        painter = painterResource(id = imageId),
        contentDescription = "Logo",
        modifier = modifier
            .clip(
                shape = CircleShape
            ),
        contentScale = ContentScale.Crop
    )
}