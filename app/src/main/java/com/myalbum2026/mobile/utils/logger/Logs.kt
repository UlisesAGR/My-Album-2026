/*
 * Logs.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.logger

import android.util.Log
import com.myalbum2026.mobile.utils.extensions.Constants.ERROR
import com.myalbum2026.mobile.utils.extensions.Constants.INFO

fun log(message: String) {
    Log.d(ERROR, message)
}

fun logInfo(message: String) {
    Log.d(INFO, message)
}

fun log(
    className: Any,
    message: String,
) {
    Log.d(className.javaClass.simpleName, message)
}
