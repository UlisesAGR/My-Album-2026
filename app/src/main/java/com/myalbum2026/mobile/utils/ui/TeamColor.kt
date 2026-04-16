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
        "SPECIALS" -> R.color.special_background
        "ALG" -> R.color.algeria_background
        "ARG" -> R.color.argentina_background
        "AUS" -> R.color.australia_background
        "AUT" -> R.color.austria_background
        "BEL" -> R.color.belgium_background
        "BIH" -> R.color.bosnia_and_herzegovina_background
        "BRA" -> R.color.brazil_background
        "CAN" -> R.color.canada_background
        "CPV" -> R.color.cape_verde_background
        "COL" -> R.color.colombia_background
        "CGO" -> R.color.congo_background
        "CRO" -> R.color.croatia_background
        "CUW" -> R.color.curacao_background
        "CZE" -> R.color.czech_republic_background
        "ECU" -> R.color.ecuador_background
        "EGY" -> R.color.egypt_background
        "ENG" -> R.color.england_background
        "FRA" -> R.color.france_background
        "GER" -> R.color.germany_background
        "GHA" -> R.color.ghana_background
        "HAI" -> R.color.haiti_background
        "IRN" -> R.color.iran_background
        "IRQ" -> R.color.iraq_background
        "CIV" -> R.color.ivory_coast_background
        "JPN" -> R.color.japan_background
        "JOR" -> R.color.jordan_background
        "KOR" -> R.color.korea_background
        "MEX" -> R.color.mexico_background
        "MAR" -> R.color.morocco_background
        "NED" -> R.color.netherlands_background
        "NZL" -> R.color.new_zealand_background
        "NOR" -> R.color.norway_background
        "PAN" -> R.color.panama_background
        "PAR" -> R.color.paraguay_background
        "POR" -> R.color.portugal_background
        "QAT" -> R.color.qatar_background
        "KSA" -> R.color.saudi_arabia_background
        "SCO" -> R.color.scotland_background
        "SEN" -> R.color.senegal_background
        "RSA" -> R.color.south_africa_background
        "ESP" -> R.color.spain_background
        "SWE" -> R.color.sweden_background
        "SUI" -> R.color.switzerland_background
        "TUN" -> R.color.tunisia_background
        "TUR" -> R.color.turkey_background
        "URU" -> R.color.uruguay_background
        "USA" -> R.color.usa_background
        "UZB" -> R.color.uzbekistan_background
        "TIMELINE" -> R.color.time_line_background
        "COCA-COLA" -> R.color.coca_cola_background
        else -> R.color.special_background
    }
    return ContextCompat.getColor(this, colorRes)
}
