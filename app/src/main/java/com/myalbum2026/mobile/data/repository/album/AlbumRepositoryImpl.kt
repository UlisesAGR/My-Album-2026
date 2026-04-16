/*
 * AlbumRepositoryImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.repository.album

import com.myalbum2026.mobile.data.datasource.album.AlbumLocalDataStore
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AlbumRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val albumLocalDataStore: AlbumLocalDataStore,
) : AlbumRepository {

    override suspend fun insertFullAlbum(
        teams: List<TeamEntity>,
        cards: List<CardEntity>,
    ): Flow<Unit> = flow {
        emit(albumLocalDataStore.insertFullAlbum(
            teams = teams,
            cards = cards,
        ))
    }.flowOn(dispatcher)

    override suspend fun updateCardStatus(
        cardId: String,
        newQty: Int,
        hasIt: Boolean,
    ) = albumLocalDataStore.updateCardStatus(
        cardId = cardId,
        newQty = newQty,
        hasIt = hasIt,
    )

    override fun getFullAlbum(): Flow<List<TeamWithCards>> =
        albumLocalDataStore.getFullAlbum().flowOn(dispatcher)
}
