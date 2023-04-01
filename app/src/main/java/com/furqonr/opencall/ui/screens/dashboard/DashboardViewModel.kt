package com.furqonr.opencall.ui.screens.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.furqonr.opencall.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class DashboardViewModel : ViewModel() {
    private val _auth = Firebase.auth
    private val _firestore = Firebase.firestore

    fun getUser() = _auth.currentUser?.displayName

    fun signOut() = _auth.signOut()

    private val _currentUser = MutableStateFlow(_auth.currentUser)
    val currentUser = _currentUser


    fun getChats() {
        _firestore.collection("chats").addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            snapshot?.let {
                for (document in it) {
                    println("${document.id} => ${document.data}")
                }
            }
        }
    }

    fun getChat(chatId: String) {
        _firestore.collection("chats").document(chatId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            snapshot?.let {
                println("${it.id} => ${it.data}")
            }
        }
    }

    fun getUsers(users: (List<User?>) -> Unit) {
        _firestore.collection("users").addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            snapshot?.let {
                val userList = mutableListOf<User>()
                for (document in it) {
                    if (document.id != _auth.currentUser?.uid) {
                        val user = User(
                            uid = document.id,
                            displayName = document.data["username"].toString(),
                            status = document.data["status"].toString(),
                        )
                        userList.add(user)
                    }
                }
                users(userList)
            }
        }
    }

    fun getChatList(users: (List<User?>) -> Unit) {
        _firestore.collection("users").addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            snapshot?.let {
                val userList = mutableListOf<User>()
                for (document in it) {
                    if (document.id != _auth.currentUser?.uid) {
                        val user = User(
                            uid = document.id,
                            displayName = document.data["username"].toString(),
                            status = document.data["status"].toString(),
                        )
                        userList.add(user)
                    }
                }
                users(userList)
            }
        }
    }

    fun getConversation(currentUserUid: String, userUid: String, chatId: (String) -> Unit) {
        _firestore.collection("chats").get().addOnSuccessListener { result ->
            for (document in result) {
                val (first, second) = document.id.split("::")
                if (first == currentUserUid && second == userUid || first == userUid && second == currentUserUid) {
                    chatId(document.id)
                } else {
                    chatId("${currentUserUid}::${userUid}")
                }
            }
        }
    }
}