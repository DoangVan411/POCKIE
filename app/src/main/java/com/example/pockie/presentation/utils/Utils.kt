package com.example.pockie.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun getTime(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(date)
    }

    fun getChatId(senderId: String, receiverId: String): String {
        return if(senderId < receiverId) "$senderId-$receiverId" else "$receiverId-$senderId"
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

}