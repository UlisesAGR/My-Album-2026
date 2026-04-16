/*
 * TeamColor.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.content.Context
import androidx.core.content.ContextCompat
import com.myalbum2026.mobile.R

fun Context.getTeamColor(teamId: String?): Int {
    val colorRes = when (teamId) {
        "MEX" -> R.color.mexico_background
        "ARG" -> R.color.argentina_background
        else -> R.color.special_background
    }
    return ContextCompat.getColor(this, colorRes)
}
