package com.example.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .height(rspDp(120.dp))
            .padding(horizontal = rspDp(20.dp))
            .fillMaxHeight(0.15f),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Beany",
            style = TextStyle(
                fontFamily = Kare,
                color = White,
                fontSize = rspSp(50.sp)
            )
        )
    }
}