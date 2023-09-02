package com.implementing.cozyspace.util

import android.util.Patterns

fun String.isValidUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}