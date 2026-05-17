/*
 * CardsItem.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity

sealed class CardsItem {
    data object Publicity : CardsItem()
    data class Progress(
        val type: CardType,
        val percentage: String,
        val missing: String,
        val obtained: String,
        val repeated: String = "",
        val total: String,
    ) : CardsItem()
    data class TeamHeader(
        val type: CardType,
        val team: TeamEntity,
        val count: Int? = null,
        val missing: Int = 0,
        val obtained: Int = 0,
        val total: Int? = null,
        val progress: Int = 0,
    ) : CardsItem()
    data class Card(
        val card: CardEntity,
    ) : CardsItem()
}
