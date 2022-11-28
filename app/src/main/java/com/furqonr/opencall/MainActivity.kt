package com.furqonr.opencall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.furqonr.opencall.ui.Navigation
import com.furqonr.opencall.ui.theme.OpenCallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCallTheme {
                Navigation()
            }
        }
    }
}