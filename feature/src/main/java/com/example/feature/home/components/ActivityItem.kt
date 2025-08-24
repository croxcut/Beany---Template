package com.example.feature.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.GlacialIndifferenceBold
import com.example.core.utils.rspSp
import com.example.data.model.ActivityEntity
import java.text.SimpleDateFormat

@Composable
fun ActivityItem(activity: ActivityEntity, formatter: SimpleDateFormat) {
    Column {
        Row {
            Text(
                text = "Activity:",
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Date",
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )
        }

        Row {
            Text(
                text = activity.activity,
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatter.format(activity.date),
                style = TextStyle(
                    fontSize = rspSp(15.sp),
                    color = Brown1,
                    fontFamily = GlacialIndifferenceBold
                )
            )
        }
    }
}