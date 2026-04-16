/*
 * CardsObtainedViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CardsObtainedViewModel @Inject constructor(
    private val repository: AlbumRepository,
) : ViewModel() {

    val uiState: StateFlow<List<CardsMissingItem>> = repository.getFullAlbum()
        .map { teamsWithCards ->
            val items = mutableListOf<CardsMissingItem>()

            // 2. Agregamos publicidad opcional
            items.add(CardsMissingItem.Publicity(url = "https://tu-link-de-ads.com"))

            // 1. Agregamos el item de Progreso al inicio
            val totalCards = teamsWithCards.sumOf { it.team.totalCards }
            val obtainedCards = teamsWithCards.sumOf { list ->
                list.cards.count { it.obtained }
            }
            val missingCount = totalCards - obtainedCards
            val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0

            items.add(
                CardsMissingItem.Progress(
                    percentage = "$percentage%",
                    total = totalCards.toString(),
                    missing = missingCount.toString()
                )
            )

            // 3. Mapeamos cada equipo que tenga cartas faltantes
            val teamItems = teamsWithCards.mapNotNull { teamWithCards ->
                // CAMBIO CLAVE: Ahora filtramos las que SÍ tienes (it.obtained)
                val obtainedInTeam = teamWithCards.cards.filter { it.obtained }

                if (obtainedInTeam.isNotEmpty()) {
                    CardsMissingItem.Cards(
                        team = teamWithCards.team,
                        dates = obtainedInTeam
                    )
                } else {
                    null // Si no tienes ninguna de este equipo, no lo mostramos en esta lista
                }
            }

            items.addAll(teamItems)
            items
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateCardQuantity(
        card: CardEntity,
        quantity: Int,
    ) = viewModelScope.launch {
        try {
            repository.updateCardStatus(
                cardId = card.id,
                quantity = quantity,
                hasIt = quantity > 0
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
