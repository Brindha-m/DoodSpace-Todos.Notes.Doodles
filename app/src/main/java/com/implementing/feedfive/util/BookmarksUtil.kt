package com.implementing.feedfive.util

import android.util.Patterns

fun String.isValidUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}