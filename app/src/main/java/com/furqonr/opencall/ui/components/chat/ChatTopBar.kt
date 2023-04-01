package com.furqonr.opencall.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.theme.Typography
import com.furqonr.opencall.ui.utils.DateConverter
import com.furqonr.opencall.R

// TODO: add navigation to profile
// TODO: when navigation back to chat, keyboard should be hidden first before navigating
@Composable
fun ChatTopBar(
    user: User? = null,
    navController: NavController,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Surface(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                shape = CircleShape,
                color = Color.Gray
            ) {
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                user?.displayName?.let { text ->
                    Text(
                        text = if (text.length > 13) {
                            text.substring(0, 13) + "...".capitalize(Locale.current)
                        } else {
                            text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.ROOT) else it.toString() }
                        },
                        style = Typography.h6
                    )
                }
                user?.status?.let {
                    Text(
                        text = if (it == "online") it else {
                            DateConverter(it).convert()
                        }, style = Typography.caption
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_videocam_24),
                            contentDescription = "Video Call Icon"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_call_24),
                            contentDescription = "Voice Call Icon"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                            contentDescription = "More menu"
                        )
                    }
                }
            }
        }
    }
}