package com.implementing.cozyspace.model

data class Calendar (
    val id: Long,
    val name: String,
    val account: String,
    val color: Int,
    val included: Boolean = true
)