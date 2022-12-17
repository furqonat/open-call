package com.furqonr.opencall.ui.screens.intro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.furqonr.opencall.ui.components.intro.SignInWithGoogleButton
import com.furqonr.opencall.ui.theme.Green700
import com.furqonr.opencall.ui.theme.Typography
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Facebook

@Composable
fun SignIn(
    onSignInClick: (username: String?) -> Unit = {},
    onGoogleClick: () -> Unit = {},
    disabled: Boolean = false
) {
    val configuration = LocalConfiguration.current
    val displayHeight = configuration.screenHeightDp

    val (username, setUsername) = remember { mutableStateOf("") }

    ConstraintLayout {
        val (topLayout, middleLayout) = createRefs()
        Surface(
            modifier = Modifier
                .height(displayHeight.dp / 2)
                .fillMaxWidth()
                .constrainAs(topLayout) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = Green700,
        ) {

        }
        Card(
            modifier = Modifier
                .constrainAs(middleLayout) {
                    top.linkTo(topLayout.bottom, margin = (-80).dp)
                    start.linkTo(parent.start, margin = (-20).dp)
                    end.linkTo(parent.end, margin = (-20).dp)
                }
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        setUsername(it)
                    },
                    label = { Text(text = "Your name") },
                    placeholder = {
                        Text(text = "Please don't use a real name")
                    }
                )

                Button(
                    shape = RoundedCornerShape(20.dp),
                    enabled = !disabled && username.trim().isNotEmpty(),
                    onClick = {
                        if (username.isNotBlank()) {
                            onSignInClick(username)
                        }
                    }) {
                    if (disabled) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Sign In", style = Typography.button)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                ) {
                    Divider(
                        modifier = Modifier
                            .height(1.dp)
                            .weight(1f)
                    )
                    Text(
                        text = "or",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .height(1.dp)
                            .weight(1f)
                    )
                }

                Text(
                    text = "Sign in with",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(start = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp),
                        shape = CircleShape,
                        backgroundColor = Color.Blue
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Brands.Facebook,
                            contentDescription = "Facebook icon",
                            modifier = Modifier
                                .padding(8.dp)
                                .width(24.dp),
                            tint = Color.White
                        )
                    }
                    SignInWithGoogleButton {
                        onGoogleClick()
                    }
                }
            }
        }
    }
}