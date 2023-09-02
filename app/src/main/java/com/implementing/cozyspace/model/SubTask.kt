package com.implementing.cozyspace.model

import java.util.UUID

data class SubTask (
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val isCompleted: Boolean
)