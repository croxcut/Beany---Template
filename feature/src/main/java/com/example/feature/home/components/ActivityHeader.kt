package com.example.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Etna
import com.example.core.ui.theme.GlacialIndifference
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.feature.home.viewmodel.HomePageViewModel

@Composable
fun ActivityHeader(viewModel: HomePageViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = rspDp(10.dp))
    ) {
        Text(
            text = "Activity Status",
            style = TextStyle(
                fontFamily = Etna,
                color = Brown1,
                fontSize = rspSp(20.sp)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Clear Activity",
            style = TextStyle(
                fontFamily = GlacialIndifference,
                color = Brown1,
                fontSize = rspSp(15.sp)
            ),
            modifier = Modifier
                .clickable { viewModel.clearAll() }
        )
    }
}