package com.implementing.cozyspace.firebase.remoteConfig.model

import com.google.gson.annotations.SerializedName


data class EventModel(
    @field:SerializedName("imageUrl")
    val imageUrl: String? = null
)