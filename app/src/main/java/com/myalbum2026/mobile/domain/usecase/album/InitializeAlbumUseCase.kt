/*
 * InitializeAlbumUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase.album

import android.content.res.AssetManager
import com.google.gson.Gson
import com.myalbum2026.mobile.domain.model.AlbumDataResponse
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InitializeAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository,
    private val assetManager: AssetManager,
    private val gson: Gson,
) {
    suspend operator fun invoke(): Flow<Unit> {
        val jsonString = assetManager.open("album_2026.json")
            .bufferedReader()
            .use { json -> json.readText() }

        val response = gson.fromJson(jsonString, AlbumDataResponse::class.java)

        val teamEntities = response.teams.map { data -> data.toTeamEntity() }

        val cardEntities = response.teams.flatMap { team ->
            team.cards.map { card -> card.toCardEntity(team.teamId) }
        }

        return repository.insertFullAlbum(
            teams = teamEntities,
            cards = cardEntities,
        )
    }
}
