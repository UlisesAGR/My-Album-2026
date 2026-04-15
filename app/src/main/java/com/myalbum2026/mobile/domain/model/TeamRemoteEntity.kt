package com.myalbum2026.mobile.domain.model

import com.google.gson.annotations.SerializedName
import com.myalbum2026.mobile.data.model.TeamEntity

data class TeamRemoteEntity(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("countryName") val countryName: String,
    @SerializedName("logoUrl") val logoUrl: String,
    @SerializedName("totalCards") val totalCards: Int,
    @SerializedName("cards") val cards: List<CardRemoteEntity>
) {
    fun toTeamEntity() = TeamEntity(
        id = teamId,
        countryName = countryName,
        logoUrl = logoUrl,
        totalCards = totalCards
    )
}