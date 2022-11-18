package com.furqonr.opencall.ui.screens.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.furqonr.opencall.R
import com.furqonr.opencall.ui.theme.Green700
import com.furqonr.opencall.ui.theme.Typography

@Composable
fun Welcome(
    onNextClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Green700),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = "Welcome asset",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(height / 2)
            )
            Text(
                text = "Welcome to OpenCall",
                style = Typography.h6,
                color = Color.White
            )
            Text(
                text = "A simple app to make a call without any personal information needed",
                style = Typography.body1,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { onNextClick() }
            ) {
                Text(text = "Next")
            }
        }
    }
}