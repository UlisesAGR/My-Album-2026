/*
 * CardsViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.cards.viewmodel

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
class CardsViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
) : ViewModel() {

    private var _cardsUiState = MutableStateFlow(CardsUiState())
    val cardsUiState: StateFlow<CardsUiState> = _cardsUiState.asStateFlow()

    private var _cardsUiEvent = MutableStateFlow<CardsUiEvent>(CardsUiEvent.Idle)
    val cardsUiEvent: StateFlow<CardsUiEvent> = _cardsUiEvent.asStateFlow()

    fun getFullAlbum(
        cardType: CardType,
        teamId: String,
    ) = viewModelScope.launch {
        _cardsUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _cardsUiState.update { state -> state.copy(isLoading = false) }
                _cardsUiEvent.emit(CardsUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(
                    cardType = cardType,
                    teamId = teamId,
                    teamsWithCards = items,
                )
                delay(DELAY)
                _cardsUiState.update { state -> state.copy(items = items) }
                delay(DELAY)
                _cardsUiState.update { state -> state.copy(isLoading = false) }
            }
    }

    private fun getItems(
        cardType: CardType,
        teamId: String,
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsItem> {

        val items = mutableListOf<CardsItem>()

        val filteredTeamsWithCards = if (teamId.isEmpty()) {
            teamsWithCards
        } else {
            teamsWithCards.filter { data -> data.team.id == teamId }
        }

        if (filteredTeamsWithCards.isNotEmpty()) {
            items.add(CardsItem.Publicity)

            val totalCards = filteredTeamsWithCards.sumOf { it.team.totalCards }
            val obtainedCards = filteredTeamsWithCards.sumOf { list -> list.cards.count { card ->
                if (cardType == CardType.MISSING) !card.obtained else card.obtained
            } }
            val missingCount = totalCards - obtainedCards
            val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0

            items.add(
                CardsItem.Progress(
                    type = cardType,
                    percentage = "$percentage%",
                    total = totalCards.toString(),
                    missing = missingCount.toString(),
                    obtained = (totalCards - missingCount).toString(),
                )
            )

            filteredTeamsWithCards.forEach { teamWithCards ->
                val filteredCards = teamWithCards.cards.filter { card ->
                    if (cardType == CardType.MISSING) !card.obtained else card.obtained
                }
                if (filteredCards.isNotEmpty()) {
                    items.add(
                        CardsItem.TeamHeader(
                            type = cardType,
                            team = teamWithCards.team,
                            count = filteredCards.size,
                            total = teamWithCards.team.totalCards,
                        )
                    )

                    filteredCards.forEach { cardEntity ->
                        items.add(CardsItem.Card(card = cardEntity))
                    }
                }
            }
        }

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
            _cardsUiEvent.emit(CardsUiEvent.ShowError(exception = exception))
        }.collect {
            _cardsUiEvent.emit(CardsUiEvent.CardUpdated)
        }
    }

    fun getMissingCardsFormattedText(): String? {
        val items = _cardsUiState.value.items
        if (items?.none { data -> data is CardsItem.Card } == true) return ""
        val body = items?.joinToString("") { item ->
            when (item) {
                is CardsItem.TeamHeader -> "\n*${item.team.id}:* "
                is CardsItem.Card -> "${item.card.number}, "
                else -> ""
            }
        }
        return body?.trim()
    }
}
