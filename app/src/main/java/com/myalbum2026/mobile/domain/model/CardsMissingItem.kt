/*
 * CardsMissingItem.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity

sealed class CardsMissingItem {
    data object Publicity : CardsMissingItem()
    data class Progress(
        val percentage: String,
        val total: String,
        val missing: String,
        val obtained: String,
    ) : CardsMissingItem()
    data class TeamHeader(
        val type: CardType,
        val team: TeamEntity,
        val count: Int,
        val total: Int,
    ) : CardsMissingItem()
    data class Card(
        val card: CardEntity
    ) : CardsMissingItem()
}
