/*
 * CardsObtainedViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.domain.usecase.album.GetFullAlbumUseCase
import com.myalbum2026.mobile.domain.usecase.album.UpdateCardUseCase
import com.myalbum2026.mobile.utils.extensions.Constants.DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CardsObtainedViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
) : ViewModel() {

    private var _cardsObtainedUiState = MutableStateFlow(CardsObtainedUiState())
    val cardsObtainedUiState: StateFlow<CardsObtainedUiState> = _cardsObtainedUiState.asStateFlow()

    private var _cardsObtainedUiEvent = MutableStateFlow<CardsObtainedUiEvent>(CardsObtainedUiEvent.Idle)
    val cardsObtainedUiEvent: StateFlow<CardsObtainedUiEvent> = _cardsObtainedUiEvent.asStateFlow()

    init {
        getFullAlbum()
    }

    fun getFullAlbum() = viewModelScope.launch {
        _cardsObtainedUiState.update { state -> state.copy(isLoading = true) }
        delay(DELAY)
        getFullAlbumUseCase()
            .catch { exception ->
                _cardsObtainedUiState.update { state -> state.copy(isLoading = false) }
                _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(teamsWithCards = items)
                _cardsObtainedUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        items = items,
                    )
                }
            }
    }

    private fun getItems(
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsMissingItem> {
        val items = mutableListOf<CardsMissingItem>()

        items.add(CardsMissingItem.Publicity)

        val totalCards = teamsWithCards.sumOf { it.team.totalCards }
        val obtainedCards = teamsWithCards.sumOf { list ->
            list.cards.count { it.obtained }
        }

        val missingCount = totalCards - obtainedCards
        val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0
        val obtained = totalCards - missingCount

        items.add(
            CardsMissingItem.Progress(
                percentage = "$percentage%",
                total = totalCards.toString(),
                missing = missingCount.toString(),
                obtained = obtained.toString(),
            )
        )

        val teamItems = teamsWithCards.mapNotNull { teamWithCards ->
            val missingInTeam = teamWithCards.cards.filter { it.obtained }
            if (missingInTeam.isNotEmpty()) {
                CardsMissingItem.Cards(
                    team = teamWithCards.team,
                    dates = missingInTeam,
                )
            } else null
        }

        items.addAll(elements = teamItems)
        return items
    }

    fun updateCardQuantity(
        card: CardEntity,
        quantity: Int,
    ) = viewModelScope.launch {
        updateCardUseCase(
            cardId = card.id,
            quantity = quantity,
            hasIt = quantity > 0,
        ).catch { exception ->
            _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.ShowError(exception = exception))
        }.collect {
            _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.CardUpdated)
        }
    }
}
