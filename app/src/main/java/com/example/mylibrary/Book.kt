package com.example.mylibrary

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Book(id:Int?, title:String, author:String, dateRead: Long?) {
    val id: Int? = id
    val title: String = title
    val author: String = author
    val dateRead: Long? = dateRead

    fun getFormattedDate(): String? {
        return dateRead?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
    }
}