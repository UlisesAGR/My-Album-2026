/*
 * CardsMissingItem.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity

sealed class CardsMissingItem {
    data class Publicity(val url: String) : CardsMissingItem()
    data class Progress(
        val percentage: String,
        val total: String,
        val missing: String,
    ) : CardsMissingItem()
    data class Cards(
        val team: TeamEntity,
        val dates: List<CardEntity>,
    ) : CardsMissingItem()
}
