/*
 * CardsRepeatedViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.repeated.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
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
class CardsRepeatedViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
): ViewModel() {

    private var _cardsRepeatedUiState = MutableStateFlow(CardsRepeatedUiState())
    val cardsRepeatedUiState: StateFlow<CardsRepeatedUiState> = _cardsRepeatedUiState.asStateFlow()

    private var _cardsRepeatedUiEvent = MutableStateFlow<CardsRepeatedUiEvent>(CardsRepeatedUiEvent.Idle)
    val cardsRepeatedUiEvent: StateFlow<CardsRepeatedUiEvent> = _cardsRepeatedUiEvent.asStateFlow()

    init {
        getFullAlbum()
    }

    private fun getFullAlbum() = viewModelScope.launch {
        _cardsRepeatedUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _cardsRepeatedUiState.update { state -> state.copy(isLoading = false) }
                _cardsRepeatedUiEvent.emit(CardsRepeatedUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(teamsWithCards = items)
                delay(DELAY)
                _cardsRepeatedUiState.update { state -> state.copy(items = items) }
                delay(DELAY)
                _cardsRepeatedUiState.update { state -> state.copy(isLoading = false) }
            }
    }

    private fun getItems(
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsItem> {
        val items = mutableListOf<CardsItem>()

        val allCards = teamsWithCards.flatMap { team -> team.cards }
        val totalRepeated = allCards.filter {
            card -> card.quantity > 1
        }.sumOf { card -> card.quantity - 1 }

        if (totalRepeated == 0) return items

        items.add(CardsItem.Publicity)

        teamsWithCards.forEach { teamWithCards ->
            val repeatedInTeam = teamWithCards.cards.filter { it.quantity > 1 }
            if (repeatedInTeam.isNotEmpty()) {
                items.add(
                    CardsItem.TeamHeader(
                        type = CardType.REPEATED,
                        team = teamWithCards.team,
                        count = null,
                        total = null,
                    )
                )

                repeatedInTeam.forEach { cardEntity ->
                    val cardForUi = cardEntity.copy(quantity = cardEntity.quantity - 1)
                    items.add(CardsItem.Card(card = cardForUi))
                }
            }
        }

        return items
    }

    fun updateCardQuantity(
        card: CardEntity,
        quantity: Int,
    ) = viewModelScope.launch {
        val totalQuantity = quantity + 1
        updateCardUseCase(
            cardId = card.id,
            quantity = totalQuantity,
            hasIt = true,
        ).catch { exception ->
            _cardsRepeatedUiEvent.emit(CardsRepeatedUiEvent.ShowError(exception = exception))
        }.collect {
            _cardsRepeatedUiEvent.emit(CardsRepeatedUiEvent.CardUpdated)
        }
    }

    fun getRepeatedCardsFormattedText(): String? {
        val items = _cardsRepeatedUiState.value.items
        if (items?.none { it is CardsItem.Card } == true) return ""
        val body = items?.joinToString("") { item ->
            when (item) {
                is CardsItem.TeamHeader -> "\n*${item.team.id}:* "
                is CardsItem.Card -> {
                    val count = item.card.quantity - 1
                    if (count > 1) "${item.card.number} (x$count), "
                    else "${item.card.number}, "
                }
                else -> ""
            }
        }
        return body?.trim()
    }
}
