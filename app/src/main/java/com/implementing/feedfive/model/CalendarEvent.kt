package com.implementing.feedfive.model

data class CalendarEvent (
    val id: Long,
    val title: String,
    val description: String,
    val start: Long,
    val end: Long
)