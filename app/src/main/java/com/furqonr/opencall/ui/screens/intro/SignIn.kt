package com.furqonr.opencall.ui.screens.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.furqonr.opencall.ui.components.intro.SignInWithGoogleButton
import com.furqonr.opencall.ui.theme.*
import com.google.firebase.auth.AuthResult
import compose.icons.AllIcons
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.regular.User

@Composable
fun SignIn(
    onSignInClick: (username: String?) -> Unit = {},
    onGoogleClick: (AuthResult) -> Unit = {},
    disabled: Boolean = false
) {
    val isDarkTheme = isSystemInDarkTheme()

    val (username, setUsername) = remember { mutableStateOf("") }
    val colorGradient = Brush.verticalGradient(
        colors = if (isDarkTheme) listOf(Color.Black, Color.Black) else listOf(
            Green700,
            Green500,
            Green200,
            Lime200
        )
    )
    Column(
        modifier = Modifier
            .background(brush = colorGradient)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = 20.dp,
                bottomEnd = 0.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 20.dp),
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
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = FontAwesomeIcons.Regular.User,
                            contentDescription = "Person Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )

                Button(
                    enabled = !disabled && username.trim().isNotEmpty(),
                    onClick = {
                        if (username.isNotBlank()) {
                            onSignInClick(username)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (disabled) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Sign In", style = Typography.button)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
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
                        onGoogleClick(it)
                    }
                }
            }
        }
    }
}