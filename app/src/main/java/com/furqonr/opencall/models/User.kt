package com.furqonr.opencall.models

data class User(
    val uid: String,
    val displayName: String,
    val status: String = "online",
    val allowStranger: Boolean = false,
) {
    fun compare(other: String): Boolean {
        return uid == other
    }
}
