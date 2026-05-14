/*
 * ShowQrViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ShowQrViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
): ViewModel() {

    private var _showQrUiState = MutableStateFlow(ShowQrUiState())
    val showQrUiState: StateFlow<ShowQrUiState> = _showQrUiState.asStateFlow()

    private var _showQrUiEvent = MutableStateFlow<ShowQrUiEvent>(ShowQrUiEvent.Idle)
    val showQrUiEvent: StateFlow<ShowQrUiEvent> = _showQrUiEvent.asStateFlow()

    init {
        getRepeatedStickers()
    }

    private fun getRepeatedStickers() = viewModelScope.launch {
        _showQrUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _showQrUiState.update { state -> state.copy(isLoading = false) }
                _showQrUiEvent.emit(ShowQrUiEvent.ShowError(exception))
            }
            .collect { teams ->
                val repeatedIds = teams.flatMap { team ->
                    team.cards.filter { card ->
                        card.quantity > 1
                    }
                }.map { card -> card.id }
                delay(DELAY)
                _showQrUiState.update { state -> state.copy(qrData = repeatedIds.joinToString(",")) }
                delay(DELAY)
                _showQrUiState.update { state -> state.copy(isLoading = false) }
            }
    }
}
