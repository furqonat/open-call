package com.furqonr.opencall.ui.utils

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


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

    fun time(context: Context): String {
        val date = dateString.toLong()
        val is24Hour = DateFormat.is24HourFormat(context)
        return if (is24Hour) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(date))
        } else {
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(date))
        }
    }
}