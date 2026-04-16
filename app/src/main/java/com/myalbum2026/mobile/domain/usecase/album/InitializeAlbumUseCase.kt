/*
 * InitializeAlbumUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase.album

import android.content.res.AssetManager
import com.google.gson.Gson
import com.myalbum2026.mobile.domain.model.AlbumDataResponse
import com.myalbum2026.mobile.data.model.CardEntity
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
            .use { it.readText() }

        val response = gson.fromJson(jsonString, AlbumDataResponse::class.java)
        val sections = response.sections

        val teamEntities = sections.teams.map { team -> team.toTeamEntity() }

        val allCards = mutableListOf<CardEntity>()

        sections.teams.forEach { team ->
            allCards.addAll(team.cards.map { it.toCardEntity(section = "teams", teamId = team.teamId) })
        }

        allCards.addAll(sections.specials.map { it.toCardEntity(section = "specials") })

        allCards.addAll(sections.timeline.map { it.toCardEntity(section = "timeline") })

        allCards.addAll(sections.coca.map { it.toCardEntity(section = "coca") })

        return repository.insertFullAlbum(
            teams = teamEntities,
            cards = allCards,
        )
    }
}
