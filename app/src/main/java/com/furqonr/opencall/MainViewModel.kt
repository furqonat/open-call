package com.furqonr.opencall

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {

    private val _auth = Firebase.auth
    private val _firestore = Firebase.firestore

    var currentUser: MutableState<Boolean> = mutableStateOf(_auth.currentUser != null)
    var loadingCreateNewUser: MutableState<Boolean> = mutableStateOf(false)

    fun signIn(username: String, currentUser: (FirebaseUser?) -> Unit) {
        loadingCreateNewUser.value = true
        _auth.signInAnonymously().addOnCompleteListener { authResultTask ->
            if (authResultTask.isSuccessful) {
                val changeRequest = userProfileChangeRequest {
                    displayName = username
                }
                _auth.currentUser?.updateProfile(changeRequest)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            createNewUser(_auth.currentUser) {
                                this.currentUser.value = true
                                currentUser(_auth.currentUser)
                            }
                        }
                    }
            }
        }.addOnFailureListener {
            loadingCreateNewUser.value = false
            Log.e("MainViewModel", "signIn: ${it.message}")
        }
    }

    fun signInWithGoogle(user: FirebaseUser, currentUser: (FirebaseUser?) -> Unit) {
        createNewUser(user, currentUser)
    }


    private fun createNewUser(user: FirebaseUser?, currentUser: (FirebaseUser?) -> Unit) {
        val account = hashMapOf(
            "username" to user?.displayName,
            "uid" to user?.uid,
            "status" to "online",
            "allowStranger" to false,
            "avatar" to user?.photoUrl
        )
        _firestore.collection("users").document("${user?.uid}").get().addOnCompleteListener { fUser ->
            if (fUser.isSuccessful) {
                val id = fUser.result.data?.get("uid")
                if (id != null) {
                    return@addOnCompleteListener
                } else {
                    user?.uid?.let { _firestore.collection("users").document(it).set(account) }
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                currentUser(user)
                                loadingCreateNewUser.value = false
                            } else {
                                loadingCreateNewUser.value = false
                                throw Exception("Create new user failed")
                            }
                        }
                }
            }
        }

    }

    val getUser: String? = _auth.currentUser?.displayName

    fun signOut() {
        _auth.signOut()
        currentUser.value = false
    }

}