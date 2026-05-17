/*
 * HomeViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.domain.usecase.album.GetFullAlbumUseCase
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
class HomeViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
) : ViewModel() {

    private var _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private var _homeUiEvent = MutableStateFlow<HomeUiEvent>(HomeUiEvent.Idle)
    val homeUiEvent: StateFlow<HomeUiEvent> = _homeUiEvent.asStateFlow()

    init {
        getFullAlbum()
    }

    fun getFullAlbum() = viewModelScope.launch {
        _homeUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _homeUiState.update { state -> state.copy(isLoading = false) }
                _homeUiEvent.emit(HomeUiEvent.ShowError(exception = exception))
            }
            .collect { teams ->
                val items = getItems(teamsWithCards = teams)
                val countries = getCountries(teamsWithCards = teams)
                delay(DELAY)
                _homeUiState.update { state -> state.copy(
                    items = items,
                    countries = countries,
                )}
                delay(DELAY)
                _homeUiState.update { state -> state.copy(isLoading = false) }
            }
    }

    private fun getItems(
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsItem> {
        val items = mutableListOf<CardsItem>()

        val totalCards = teamsWithCards.sumOf { card -> card.team.totalCards }
        val obtainedCards = teamsWithCards.sumOf { list ->
            list.cards.count { card -> card.obtained }
        }

        val missingCount = totalCards - obtainedCards
        val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0
        val obtained = totalCards - missingCount
        val repeatedCount = teamsWithCards.sumOf { list ->
            list.cards.sumOf { card ->
                if (card.quantity > 1) card.quantity - 1 else 0
            }
        }

        items.add(
            CardsItem.Progress(
                type = CardType.MISSING,
                percentage = "$percentage%",
                total = totalCards.toString(),
                missing = missingCount.toString(),
                obtained = obtained.toString(),
                repeated = repeatedCount.toString(),
            )
        )

        return items
    }

    private fun getCountries(
        teamsWithCards: List<TeamWithCards>,
    ): List<CardsItem.TeamHeader> =
        teamsWithCards.map { teamWithCards ->
            val missing = teamWithCards.cards.count { card -> !card.obtained }
            val obtained = teamWithCards.cards.count { card -> card.obtained }

            val totalCards = teamWithCards.cards.size
            val progressPercentage = if (totalCards > 0) {
                (obtained * 100 / totalCards)
            } else 0

            CardsItem.TeamHeader(
                type = CardType.MISSING,
                team = teamWithCards.team,
                missing = missing,
                obtained = obtained,
                total = teamWithCards.cards.size,
                progress = progressPercentage,
            )
        }
}
