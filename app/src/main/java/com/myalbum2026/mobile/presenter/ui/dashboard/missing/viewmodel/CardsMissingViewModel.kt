/*
 * CardsMissingViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.viewmodel

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
class CardsMissingViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
    private val updateCardUseCase: UpdateCardUseCase,
): ViewModel() {

    private var _cardsMissingUiState = MutableStateFlow(CardsMissingUiState())
    val cardsMissingUiState: StateFlow<CardsMissingUiState> = _cardsMissingUiState.asStateFlow()

    private var _cardsMissingUiEvent = MutableStateFlow<CardsMissingUiEvent>(CardsMissingUiEvent.Idle)
    val cardsMissingUiEvent: StateFlow<CardsMissingUiEvent> = _cardsMissingUiEvent.asStateFlow()

    init {
        getFullAlbum()
    }

    fun getFullAlbum() = viewModelScope.launch {
        _cardsMissingUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _cardsMissingUiState.update { state -> state.copy(isLoading = false) }
                _cardsMissingUiEvent.emit(CardsMissingUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(teamsWithCards = items)
                _cardsMissingUiState.update { state -> state.copy(items = items) }
                delay(DELAY)
                _cardsMissingUiState.update { state -> state.copy(isLoading = false) }
            }
    }

    private fun getItems(
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsItem> {

        val items = mutableListOf<CardsItem>()

        items.add(CardsItem.Publicity)

        val totalCards = teamsWithCards.sumOf { it.team.totalCards }
        val obtainedCards = teamsWithCards.sumOf { list -> list.cards.count { it.obtained } }
        val missingCount = totalCards - obtainedCards
        val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0

        items.add(
            CardsItem.Progress(
                percentage = "$percentage%",
                total = totalCards.toString(),
                missing = missingCount.toString(),
                obtained = (totalCards - missingCount).toString(),
            )
        )

        teamsWithCards.forEach { teamWithCards ->
            val missingInTeam = teamWithCards.cards.filter { !it.obtained }
            if (missingInTeam.isNotEmpty()) {
                items.add(
                    CardsItem.TeamHeader(
                        type = CardType.MISSING,
                        team = teamWithCards.team,
                        count = missingInTeam.size,
                        total = teamWithCards.cards.size,
                    )
                )
                missingInTeam.forEach { cardEntity ->
                    items.add(CardsItem.Card(card = cardEntity))
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
            _cardsMissingUiEvent.emit(CardsMissingUiEvent.ShowError(exception = exception))
        }.collect {
            _cardsMissingUiEvent.emit(CardsMissingUiEvent.CardUpdated)
        }
    }

    fun getMissingCardsFormattedText(title: String): String {
        val items = _cardsMissingUiState.value.items
        if (items.none { it is CardsItem.Card }) return ""
        val body = items.joinToString("") { item ->
            when (item) {
                is CardsItem.TeamHeader -> "\n*$item.team.countryName:*\n"
                is CardsItem.Card -> "${item.card.number}  "
                else -> ""
            }
        }
        return "*$title*\n\n" + body.trim()
    }
}
