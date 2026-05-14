/*
 * ScanQrViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.viewmodel

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
class ScanQrViewModel @Inject constructor(
    private val getFullAlbumUseCase: GetFullAlbumUseCase,
): ViewModel() {

    private var _scanQrUiState = MutableStateFlow(ScanQrUiState())
    val scanQrUiState: StateFlow<ScanQrUiState> = _scanQrUiState.asStateFlow()

    private var _scanQrUiEvent = MutableStateFlow<ScanQrUiEvent>(ScanQrUiEvent.Idle)
    val scanQrUiEvent: StateFlow<ScanQrUiEvent> = _scanQrUiEvent.asStateFlow()

    fun processQrData(qrData: String) = viewModelScope.launch {
        _scanQrUiState.update { state -> state.copy(isLoading = true) }
        getFullAlbumUseCase()
            .catch { exception ->
                _scanQrUiState.update { it.copy(isLoading = false) }
                _scanQrUiEvent.emit(ScanQrUiEvent.ShowError(exception))
            }
            .collect { teams ->
                val allMyCards = teams.flatMap { it.cards }
                val remoteRepeatedIds = qrData.split(",")
                val matchCards = allMyCards.filter { myCard ->
                    myCard.quantity == 0 && remoteRepeatedIds.contains(myCard.id)
                }
                delay(DELAY)
                _scanQrUiEvent.emit(ScanQrUiEvent.ComparisonSuccess(matchCards = matchCards))
                delay(DELAY)
                _scanQrUiState.update { state -> state.copy(isLoading = false) }
            }
    }
}
