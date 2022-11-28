package com.furqonr.opencall.ui.components.dashboard

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardViewModel : ViewModel() {
    private val _auth = Firebase.auth

    fun getUser() = _auth.currentUser?.displayName

    fun signOut() = _auth.signOut()
}