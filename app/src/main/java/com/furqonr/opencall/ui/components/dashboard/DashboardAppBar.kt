package com.furqonr.opencall.ui.components.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.furqonr.opencall.R
import com.furqonr.opencall.ui.components.keyboardState.KeyboardState
import com.furqonr.opencall.ui.components.keyboardState.rememberKeyboardState
import com.furqonr.opencall.ui.theme.Green700

@Composable
fun DashboardAppBar(
    onSearch: (String) -> Unit,
    addIconClick: () -> Unit = {}
) {
    Row(modifier = Modifier.padding(4.dp)) {
        InputSearch(
            onSearch = {
                onSearch(it)
            },
            addIconClick = addIconClick
        )
    }
}

@Composable
private fun InputSearch(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    addIconClick: () -> Unit = {},
) {
    val (text, setText) = remember {
        mutableStateOf("")
    }
    val placeholder = remember {
        mutableStateOf("Search chat")
    }
    val (isFocused, setFocused) = remember {
        mutableStateOf(false)
    }
    val (animationState, setAnimationState) = remember {
        mutableStateOf(false)
    }

    val isKeyboardVisible = rememberKeyboardState()

    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .border(
                    1.dp,
                    color = if (!isFocused) Color.Gray else Green700,
                    shape = RoundedCornerShape(30)
                ),
            shape = RoundedCornerShape(30)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(4.dp)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_search_24),
                    contentDescription = "search",
                    modifier = Modifier.padding(start = 4.dp)
                )
                BasicTextField(
                    value = text,
                    onValueChange = {
                        setText(it)
                        onSearch(it)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged {
                            setFocused(it.isFocused)
                            setAnimationState(it.isFocused)
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(text)
                        },
                    ),
                    textStyle = TextStyle(
                        color = isFocused.takeIf { it }?.let { Color.Black } ?: Color.Gray
                    ),
                    // add placeholder
                    decorationBox = {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder.value,
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                        it()
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = !animationState,
            enter = slideInHorizontally(
                initialOffsetX = { 100 }
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { 100 }
            )
        ) {
            IconButton(
                modifier = Modifier
                    .padding(8.dp),
                onClick = addIconClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_chat_24),
                    contentDescription = "new chat",
                    modifier = Modifier
                        .size(24.dp)
                )
            }

        }
    }

    LaunchedEffect(isKeyboardVisible.value) {
        if (isKeyboardVisible.value == KeyboardState.CLOSED) {
            setFocused(false)
            setAnimationState(false)
            focusManager.clearFocus()
        } else {
            setFocused(true)
            setAnimationState(true)
        }
    }

}
