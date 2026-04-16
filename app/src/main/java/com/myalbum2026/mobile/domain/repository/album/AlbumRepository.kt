/*
 * AlbumRepository.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.repository.album

import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun insertFullAlbum(
        teams: List<TeamEntity>,
        cards: List<CardEntity>,
    ): Flow<Unit>
    suspend fun updateCardStatus(
        cardId: String,
        quantity: Int,
        hasIt: Boolean,
    )
    fun getFullAlbum(): Flow<List<TeamWithCards>>
}
