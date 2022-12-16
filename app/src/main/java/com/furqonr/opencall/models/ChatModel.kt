package com.furqonr.opencall.models

import com.google.gson.annotations.SerializedName


data class ChatModel(
    var uid: String,
    var message: String,
    var timestamp: Long,
    var type: String,
    var sender: User,
    var receiver: User,
    @SerializedName("read")
    var isRead: Boolean = false,
    @SerializedName("sent")
    var isSent: Boolean = false,
    @SerializedName("delivered")
    var isDelivered: Boolean = false,
    @SerializedName("seen")
    var isSeen: Boolean = false,
    @SerializedName("deleted")
    var isDeleted: HashMap<String, Boolean>,
)