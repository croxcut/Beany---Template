package com.example.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.core.composables.Footer
import com.example.core.ui.theme.Brown1
import com.example.core.ui.theme.Kare
import com.example.core.ui.theme.White
import com.example.core.utils.rspDp
import com.example.core.utils.rspSp
import com.example.data.model.ActivityEntity
import com.example.domain.model.Route
import com.example.feature.home.viewmodel.HomePageViewModel

@Composable
fun ActivityStatusSection(
    activityList: List<ActivityEntity>,
    viewModel: HomePageViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActivityHeader(viewModel = viewModel)
        ActivityList(activityList = activityList)

        Spacer(modifier = Modifier.height(rspDp(50.dp)))

        Text(
            text = "Beany",
            style = TextStyle(
                fontFamily = Kare,
                fontSize = rspSp(20.sp),
                color = Brown1
            )
        )

        Footer(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = rspDp(100.dp)),
            onClick = { navController.navigate(Route.AboutUsPage.route) }
        )
    }
}