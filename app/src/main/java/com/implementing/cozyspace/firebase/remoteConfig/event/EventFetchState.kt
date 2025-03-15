package com.implementing.cozyspace.firebase.remoteConfig.event

import com.implementing.cozyspace.firebase.remoteConfig.model.EventModel

sealed class EventFetchState {
    data class Success(val events: List<EventModel>) : EventFetchState()
    object Loading : EventFetchState()
    data class Error(val errorMessage: String) : EventFetchState()
}