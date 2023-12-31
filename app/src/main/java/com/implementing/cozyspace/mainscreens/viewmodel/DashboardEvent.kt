package com.implementing.cozyspace.mainscreens.viewmodel

import com.implementing.cozyspace.model.Task


sealed class DashboardEvent {
    data class ReadPermissionChanged(val hasPermission: Boolean) : DashboardEvent()
    data class UpdateTask(val task: Task) : DashboardEvent()
    object InitAll : DashboardEvent()
}