package com.furqonr.opencall.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.furqonr.opencall.R

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
fun ChatInput(
    onSent: (String) -> Unit = {},
) {
    // request focus on start
    val (text, setText) = remember {
        mutableStateOf("")
    }
    val (isFocus, setFocus) = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics {
                contentDescription = ""
                keyboardShownProperty = isFocus
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
                .height(48.dp)

        ) {
            var lastFocusState by remember { mutableStateOf(false) }
            BasicTextField(
                value = text,
                onValueChange = { setText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .align(Alignment.CenterStart)
                    .onFocusChanged { state ->
                        if (lastFocusState != state.isFocused) {
                            setFocus(state.isFocused)
                        }
                        lastFocusState = state.isFocused
                    },
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
            )
            val disableContentColor =
                MaterialTheme.colorScheme.onSurfaceVariant
            if (text.isEmpty() && !isFocus) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 32.dp),
                    text = "Type a message",
                    style = MaterialTheme.typography.bodyLarge.copy(color = disableContentColor)
                )
            }

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
}