/*
 * TeamRemoteEntity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.google.gson.annotations.SerializedName
import com.myalbum2026.mobile.data.model.TeamEntity

data class TeamRemoteEntity(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("countryName") val countryName: String,
    @SerializedName("flagResource") val flagResource: String,
    @SerializedName("totalCards") val totalCards: Int,
    @SerializedName("cards") val cards: List<CardRemoteEntity>,
) {
    fun toTeamEntity(index: Int) = TeamEntity(
        id = teamId,
        countryName = countryName,
        flagResource = flagResource,
        totalCards = totalCards,
        displayOrder = index,
    )
}
