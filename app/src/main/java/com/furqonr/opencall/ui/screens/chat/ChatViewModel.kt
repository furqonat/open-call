package com.furqonr.opencall.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel() {

    private val _firebaseAuth = Firebase.auth
    private val _firebaseUser = _firebaseAuth.currentUser
    private val _firestrore = Firebase.firestore

    fun getCurrentUser(uid: String) = _firebaseUser?.uid == uid

    val currentUser = _firebaseUser


    fun getUser(uid: String, user: (User) -> Unit) {
        _firestrore.collection("users").document(uid).get().addOnSuccessListener {
            user(
                User(
                    uid = it["uid"].toString(),
                    displayName = it["username"].toString(),
                    status = it["status"].toString(),
                    allowStranger = it["allowStranger"].toString().toBoolean()
                )
            )
        }
    }

    fun getChats(uid: String, limit: Int = 20, chats: (List<ChatModel>) -> Unit) {
        _firestrore.collection("chats").document(uid).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                } else {
                    val chatModelList = mutableListOf<ChatModel>()
                    snapshot?.let { snap ->
                        for (document in snap) {
                            chatModelList.add(
                                ChatModel(
                                    uid = document.id,
                                    message = document["message"].toString(),
                                    timestamp = document["timestamp"].toString().toLong(),
                                    type = document["type"].toString(),
                                    sender = document["sender"].toString(),
                                    receiver = document["receiver"].toString(),
                                    isRead = document["read"].toString().toBoolean(),
                                    isSent = document["sent"].toString().toBoolean(),
                                    isDeleted = mapOf(
                                        "sender" to document["deleted.sender"].toString()
                                            .toBoolean(),
                                        "receiver" to document["deleted.receiver"].toString()
                                            .toBoolean()
                                    ) as HashMap<String, Boolean>,
                                    isDelivered = document["delivered"].toString().toBoolean(),
                                    isSeen = document["seen"].toString().toBoolean(),
                                )
                            )
                        }
                    }
                    chats(chatModelList.sortedByDescending { it.timestamp })
                }
            }
    }

    fun sendMessage(
        message: String,
        chatId: String,
        sender: String,
        receiver: String,
        chatResult: (ChatModel) -> Unit,
    ) {
        val chatModel =
            ChatModel(
                uid = chatId,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = "text",
                sender = sender,
                receiver = receiver,
                isRead = false,
                isSent = false,
                isDelivered = false,
                isSeen = false,
                isDeleted = hashMapOf(
                    sender to false,
                    receiver to false
                )
            )
        chatResult(chatModel)
        _firestrore.collection("chats").document(chatId).collection("messages").add(chatModel)
            .addOnSuccessListener { doc ->
                _firestrore.collection("chats").document(chatId).collection("messages")
                    .document(doc.id).update("sent", true)
            }.addOnFailureListener {
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firestrore.collection("chats").document(chatId).set({
                        "lastMessage" to chatModel.message
                        "lastMessageTimestamp" to chatModel.timestamp
                        "lastMessageSender" to chatModel.sender
                        "lastMessageReceiver" to chatModel.receiver
                    })
                } else {
                    Log.d("ChatViewModel", "sendMessage: Failed")
                }
            }
    }

    fun getMessageStatus(messageId: String, chatId: String, messageStatus: (Boolean) -> Unit) {
        _firestrore.collection("chats").document(chatId).collection("messages").document(messageId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                } else {
                    snapshot?.let { snap ->
                        messageStatus(snap["sent"].toString().toBoolean())
                    }
                }
            }
    }
}

