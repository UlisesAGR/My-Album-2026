/*
 * AlbumLocalDataSourceImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.datasource.album

import com.myalbum2026.mobile.data.database.AlbumDao
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AlbumLocalDataStoreImpl @Inject constructor(
    private val albumDao: AlbumDao,
) : AlbumLocalDataStore {

    override suspend fun insertFullAlbum(
        teams: List<TeamEntity>,
        cards: List<CardEntity>,
    ) = albumDao.insertFullAlbum(
        teams = teams,
        cards = cards,
    )

    override fun getFullAlbum(): Flow<List<TeamWithCards>> =
        albumDao.getFullAlbum()

    override suspend fun updateCardStatus(
        cardId: String,
        quantity: Int,
        hasIt: Boolean
    ) = albumDao.updateCardStatus(
        cardId = cardId,
        quantity = quantity,
        hasIt = hasIt,
    )
}
