/*
 * CountryListViewModel
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.countries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CountryListViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
) : ViewModel() {

    private var _countryListUiState = MutableStateFlow(CountryListUiState())
    val countryListUiState: StateFlow<CountryListUiState> = _countryListUiState.asStateFlow()

    private var _countryListUiEvent = MutableStateFlow<CountryListUiEvent>(CountryListUiEvent.Idle)
    val countryListUiEvent: StateFlow<CountryListUiEvent> = _countryListUiEvent.asStateFlow()

    fun getAllTeams(cardType: CardType) = viewModelScope.launch {
        _countryListUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _countryListUiState.update { state -> state.copy(isLoading = false) }
                _countryListUiEvent.emit(CountryListUiEvent.ShowError(exception))
            }
            .collect { teamsWithCards ->
                val items = mutableListOf<CardsItem.TeamHeader>()

                teamsWithCards.forEach { teamWithCards ->
                    val missingInTeam = teamWithCards.cards.filter {
                        if (cardType == CardType.MISSING) !it.obtained else it.obtained
                    }
                    val obtainedInTeamCount = teamWithCards.cards.count { card ->
                        if (cardType == CardType.MISSING) !card.obtained else card.obtained
                    }
                    if (missingInTeam.isNotEmpty()) {
                        items.add(
                            CardsItem.TeamHeader(
                                type = CardType.MISSING,
                                team = teamWithCards.team,
                                count = missingInTeam.size,
                                total = teamWithCards.cards.size,
                                progress = obtainedInTeamCount,
                            )
                        )
                    }
                }
                delay(DELAY)
                _countryListUiState.update { state -> state.copy(teams = items) }
                delay(DELAY)
                _countryListUiState.update { state -> state.copy(isLoading = false) }
            }
    }
}
