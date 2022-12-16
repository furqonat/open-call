package com.furqonr.opencall.ui.components.keyboardState

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView

enum class KeyboardState {
    OPEN,
    CLOSED
}

@Composable
fun rememberKeyboardState(): State<KeyboardState> {
    val keyboardState = remember { mutableStateOf(KeyboardState.CLOSED) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardState.OPEN
            } else {
                KeyboardState.CLOSED
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}