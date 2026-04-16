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
        "USA" -> R.color.usa_background
        "CAN" -> R.color.canada_background
        "MEX" -> R.color.mexico_background
        "ARG" -> R.color.argentina_background
        "ESP" -> R.color.spain_background
        "FRA" -> R.color.france_background
        "GER" -> R.color.germany_background
        "JPN" -> R.color.japan_background
        "MAR" -> R.color.morocco_background
        "CRO" -> R.color.croatia_background
        "BEL" -> R.color.belgium_background
        "BRA" -> R.color.croatia_background
        "NED" -> R.color.netherlands_background
        "ENG" -> R.color.england_background
        "KOR" -> R.color.korea_background
        else -> R.color.special_background
    }
    return ContextCompat.getColor(this, colorRes)
}
