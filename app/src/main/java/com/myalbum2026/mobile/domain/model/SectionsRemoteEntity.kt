package com.myalbum2026.mobile.domain.model

import com.google.gson.annotations.SerializedName

data class SectionsRemoteEntity(
    @SerializedName("specials") val specials: List<CardRemoteEntity>,
    @SerializedName("teams") val teams: List<TeamRemoteEntity>,
    @SerializedName("timeline") val timeline: List<CardRemoteEntity>,
    @SerializedName("coca") val coca: List<CardRemoteEntity>,
)
