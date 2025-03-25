package com.example.pockie.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date

object Utils {
    fun getTime(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(date)
    }
}