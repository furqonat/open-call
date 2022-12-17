package com.furqonr.opencall.ui.utils

class DateConverter(
    private val dateString: String
) {

    fun convert(): String {
        val date = dateString.toLong()
        val currentDate = System.currentTimeMillis()
        val diff = currentDate - date
        val diffSeconds = diff / 1000
        val diffMinutes = diff / (60 * 1000)
        val diffHours = diff / (60 * 60 * 1000)
        val diffDays = diff / (24 * 60 * 60 * 1000)
        return when {
            diffSeconds < 60 -> {
                "$diffSeconds seconds ago"
            }
            diffMinutes < 60 -> {
                "$diffMinutes minutes ago"
            }
            diffHours < 24 -> {
                "$diffHours hours ago"
            }
            else -> {
                "$diffDays days ago"
            }
        }
    }
}