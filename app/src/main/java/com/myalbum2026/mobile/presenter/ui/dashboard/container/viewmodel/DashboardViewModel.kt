/*
 * DashboardViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import com.myalbum2026.mobile.domain.usecase.user.IsInfoShowedUseCase
import com.myalbum2026.mobile.domain.usecase.user.SetIsInfoShowedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val isInfoShowedUseCase: IsInfoShowedUseCase,
    private val setIsInfoShowedUseCase: SetIsInfoShowedUseCase,
    repository: AlbumRepository,
) : ViewModel() {

    private var _dashboardUiEvent = MutableStateFlow<DashboardUiEvent>(DashboardUiEvent.Idle)
    val dashboardUiEvent: StateFlow<DashboardUiEvent> = _dashboardUiEvent.asStateFlow()

    init {
        checkInfoShowed()
    }

    private fun checkInfoShowed() = viewModelScope.launch {
        resetUiEvent()
        val isFirstTime = isInfoShowedUseCase().firstOrNull() ?: true
        if (isFirstTime) {
            _dashboardUiEvent.emit(DashboardUiEvent.ShowInfoDialog)
        }
    }

    fun onAcceptClicked() = viewModelScope.launch {
        setIsInfoShowedUseCase()
    }

    val uiState: StateFlow<List<CardsMissingItem>> = repository.getFullAlbum()
        .map { teamsWithCards ->
            val items = mutableListOf<CardsMissingItem>()

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

            items
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private fun resetUiEvent() = viewModelScope.launch {
        _dashboardUiEvent.emit(DashboardUiEvent.Idle)
    }
}
