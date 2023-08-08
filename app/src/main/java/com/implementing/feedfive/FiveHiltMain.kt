package com.implementing.feedfive

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FiveHiltMain : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}

// for string resources where context is not available
fun getString(
    @StringRes
    resId: Int,
    vararg args: String
) = FiveHiltMain.appContext.getString(resId, *args)
