/*
 * CardRemoteEntity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.google.gson.annotations.SerializedName
import com.myalbum2026.mobile.data.model.CardEntity

data class CardRemoteEntity(
    @SerializedName("id") val id: String,
    @SerializedName("number") val number: Int,
    @SerializedName("type") val type: String,
    @SerializedName("position") val position: String? = null
) {
    fun toCardEntity(teamId: String) = CardEntity(
        id = id,
        teamId = teamId,
        number = number,
        type = type,
        position = position,
    )
}
