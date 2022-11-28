package com.furqonr.opencall

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {

    private val _auth = Firebase.auth

    var currentUser: MutableState<Boolean> = mutableStateOf(_auth.currentUser != null)

    fun signIn(username: String, currentUser: (FirebaseUser?) -> Unit) {
        _auth.signInAnonymously().addOnCompleteListener { authResultTask ->
            if (authResultTask.isSuccessful) {
                val changeRequest = userProfileChangeRequest {
                    displayName = username
                }
                _auth.currentUser?.updateProfile(changeRequest)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            this.currentUser.value = true
                            currentUser(_auth.currentUser)
                        }
                    }

            } else {
                throw Exception("Sign in failed")
            }
        }
    }

    val getUser: String? = _auth.currentUser?.displayName

    fun signOut() {
        _auth.signOut()
        currentUser.value = false
    }

}