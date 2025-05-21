package com.example.pockie.presentation.utils

import com.example.pockie.R
import com.example.pockie.domain.model.Tag
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

    val listTag = listOf(
        Tag(
            id = 1,
            icon = R.drawable.ic_other,
            name = "Other",
        ),
        Tag(
            id = 2,
            icon = R.drawable.ic_study,
            name = "Study",
        ),
        Tag(
            id = 3,
            icon = R.drawable.ic_entertainment,
            name = "Entertaiment"
        ),
        Tag(
            id = 4,
            icon = R.drawable.ic_food,
            name = "Food"
        ),
        Tag(
            id = 5,
            icon = R.drawable.ic_school,
            name = "School"
        ),
    )



}