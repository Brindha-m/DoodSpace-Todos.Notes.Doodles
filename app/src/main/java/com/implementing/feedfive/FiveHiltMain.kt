package com.implementing.feedfive

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.implementing.feedfive.util.Constants
import dagger.hilt.android.HiltAndroidApp

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS_PREFERENCES)


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
fun getString(@StringRes resId: Int, vararg args: String) =
    FiveHiltMain.appContext.getString(resId, *args)
