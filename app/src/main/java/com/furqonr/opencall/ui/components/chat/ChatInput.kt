package com.furqonr.opencall.ui.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.furqonr.opencall.R
import com.furqonr.opencall.ui.theme.Typography

@Composable
fun ChatInput(
    onSent: (String) -> Unit = {},

) {
    // request focus on start
    val focusRequester = FocusRequester()
    val (text, setText) = remember {
        mutableStateOf("")
    }
    val (isFocus, setFocus) = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    setText(it)
                },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        setFocus(it.isFocused)
                    },
                textStyle = TextStyle(
                    color = if (isFocus) Color.Black else Color.Gray,
                    fontSize = 16.sp
                ),
                decorationBox = {
                    if (text.isEmpty()) {
                        Text(
                            text = "Type a message",
                            color = Color.LightGray,
                            style = Typography.body1
                        )
                    } else {
                        it()
                    }
                }
            )
        }
        IconButton(onClick = {
            onSent(text)
            setText("")
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_send_24),
                contentDescription = "Icon send"
            )
        }
    }
    SideEffect {
        focusRequester.requestFocus()
    }
}