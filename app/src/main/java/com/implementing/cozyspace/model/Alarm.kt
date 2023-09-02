package com.implementing.cozyspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "alarms")
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val time: Long,

)