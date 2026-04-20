/*
 * DashboardViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.domain.usecase.album.GetFullAlbumUseCase
import com.myalbum2026.mobile.domain.usecase.user.IsInfoShowedUseCase
import com.myalbum2026.mobile.domain.usecase.user.SetIsInfoShowedUseCase
import com.myalbum2026.mobile.utils.extensions.Constants.DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val isInfoShowedUseCase: IsInfoShowedUseCase,
    private val setIsInfoShowedUseCase: SetIsInfoShowedUseCase,
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
) : ViewModel() {

    private var _dashboardUiState = MutableStateFlow(DashboardUiState())
    val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()

    private var _dashboardUiEvent = MutableStateFlow<DashboardUiEvent>(DashboardUiEvent.Idle)
    val dashboardUiEvent: StateFlow<DashboardUiEvent> = _dashboardUiEvent.asStateFlow()

    init {
        checkInfoShowed()
    }

    private fun checkInfoShowed() = viewModelScope.launch {
        _dashboardUiState.update { state -> state.copy(isLoading = true) }
        val isFirstTime = isInfoShowedUseCase().firstOrNull() ?: false
        if (!isFirstTime) {
            _dashboardUiEvent.emit(DashboardUiEvent.ShowInfoDialog)
        }
        getFullAlbum()
    }

    fun onAcceptClicked() = viewModelScope.launch {
        setIsInfoShowedUseCase()
    }

    fun getFullAlbum() = viewModelScope.launch {
        getFullAlbumUseCase()
            .catch { exception ->
                _dashboardUiState.update { state -> state.copy(isLoading = false) }
                _dashboardUiEvent.emit(DashboardUiEvent.ShowError(exception = exception))
            }
            .collect { items ->
                val items = getItems(teamsWithCards = items)
                _dashboardUiState.update { state -> state.copy(items = items) }
                delay(DELAY)
                _dashboardUiState.update { state -> state.copy(isLoading = false) }
            }
    }

    private fun getItems(
        teamsWithCards: List<TeamWithCards>,
    ): MutableList<CardsItem> {
        val items = mutableListOf<CardsItem>()

        val totalCards = teamsWithCards.sumOf { it.team.totalCards }
        val obtainedCards = teamsWithCards.sumOf { list ->
            list.cards.count { it.obtained }
        }

        val missingCount = totalCards - obtainedCards
        val percentage = if (totalCards > 0) (obtainedCards * 100 / totalCards) else 0
        val obtained = totalCards - missingCount

        items.add(
            CardsItem.Progress(
                percentage = "$percentage%",
                total = totalCards.toString(),
                missing = missingCount.toString(),
                obtained = obtained.toString(),
            )
        )

        return items
    }
}
