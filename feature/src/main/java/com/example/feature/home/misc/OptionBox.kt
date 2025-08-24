package com.example.feature.home.misc

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp

@Composable
fun RowScope.OptionBox(
    onClick: () -> Unit,
    iconRes: Int,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .optionsContainerConfig()
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = title,
            modifier = Modifier
                .size(rspDp(80.dp))
                .offset(x = rspDp(80.dp), y = rspDp(18.dp))
        )

        Column(modifier = Modifier.padding(all = rspDp(10.dp))) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Etna,
                    color = Brown1,
                    fontSize = rspSp(20.sp)
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = GlacialIndifference,
                    color = Brown1
                ),
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}