/*
 * SplashViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.domain.usecase.album.InitializeAlbumUseCase
import com.myalbum2026.mobile.domain.usecase.user.IsFirstTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isFirstTimeUseCase: IsFirstTimeUseCase,
    private val initializeAlbumUseCase: InitializeAlbumUseCase
) : ViewModel() {

    private var _splashUiEvent = MutableStateFlow<SplashUiEvent>(SplashUiEvent.Idle)
    val splashUiEvent: StateFlow<SplashUiEvent> = _splashUiEvent.asStateFlow()

    init {
        checkInitialState()
    }

    private fun checkInitialState() = viewModelScope.launch {
        resetUiEvent()
        val isFirstTime = isFirstTimeUseCase().firstOrNull() ?: true
        if (isFirstTime) {
            initializeAlbum()
        } else {
            _splashUiEvent.emit(SplashUiEvent.GoToDashboard)
        }
    }

    private fun initializeAlbum() = viewModelScope.launch {
        initializeAlbumUseCase()
            .catch { exception ->
                _splashUiEvent.emit(SplashUiEvent.ShowError(exception = exception))
            }
            .collect {
                _splashUiEvent.emit(SplashUiEvent.GoToWelcome)
            }
    }

    private fun resetUiEvent() = viewModelScope.launch {
        _splashUiEvent.emit(SplashUiEvent.Idle)
    }
}
