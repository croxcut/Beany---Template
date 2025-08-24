package com.example.feature.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.Brown1
import com.example.core.utils.rspDp
import com.example.data.model.ActivityEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ActivityList(activityList: List<ActivityEntity>) {
    val formatter = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(rspDp(200.dp))
            .padding(horizontal = rspDp(10.dp))
            .border(
                width = rspDp(2.dp),
                color = Brown1,
                shape = RoundedCornerShape(rspDp(10.dp))
            )
            .padding(rspDp(10.dp))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(activityList) { activity ->
                ActivityItem(activity = activity, formatter = formatter)
            }
        }
    }
}