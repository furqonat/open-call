package com.furqonr.opencall.models

class User(
    val uid: String,
    val displayName: String,
    val status: String = "online",
    val allowStranger: Boolean = false,
    val avatar: String? = null
)
