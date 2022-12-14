package com.furqonr.opencall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.furqonr.opencall.ui.Navigation
import com.furqonr.opencall.ui.theme.OpenCallTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class MainActivity : ComponentActivity() {
    private val _firestore = Firebase.firestore
    private val _currentUser = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCallTheme {
                Navigation()
            }
        }
    }

    override fun onResume() {
        _firestore.collection("users").document(_currentUser?.uid.toString())
            .update("status", "online")
        super.onResume()
    }

    override fun onStop() {
        _firestore.collection("users").document(_currentUser?.uid.toString())
            .update("status", "${Date().time}")
        super.onStop()
    }

    override fun onDestroy() {
        _firestore.collection("users").document(_currentUser?.uid.toString())
            .update("status", "${Date().time}")
        super.onDestroy()
    }
}