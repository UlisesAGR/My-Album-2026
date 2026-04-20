/*
 * CardsObtainedViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel

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
        getFullAlbumUseCase()
            .catch { exception ->
                _cardsObtainedUiState.update { state -> state.copy(isLoading = false) }
                _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(teamsWithCards = items)
                _cardsObtainedUiState.update { state -> state.copy(items = items) }
                delay(DELAY)
                _cardsObtainedUiState.update { state -> state.copy(isLoading = false) }
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
            val missingInTeam = teamWithCards.cards.filter { it.obtained }
            if (missingInTeam.isNotEmpty()) {
                items.add(
                    CardsItem.TeamHeader(
                        type = CardType.OBTAINED,
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
            _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.ShowError(exception = exception))
        }.collect {
            _cardsObtainedUiEvent.emit(CardsObtainedUiEvent.CardUpdated)
        }
    }
}
