package com.example.core.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.utils.rspSp

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
        ) {
            Text(
                text = "About-Us",
                modifier = Modifier
                    .clickable{
                        onClick()
                    },
                style = footerGetTextStyle()
            )
            Text(
                text = " | ",
                style = footerGetTextStyle()
            )
            Text(
                text = "Terms and Conditions",
                style = footerGetTextStyle()
            )
            Text(
                text = "© 2025 All Rights Reserved",
                style = footerGetTextStyle()
            )
        }
    }
}

@Composable
fun footerGetTextStyle(): TextStyle {
    return TextStyle(
        fontSize = rspSp(12.sp),
        color = Color.Black
    )
}