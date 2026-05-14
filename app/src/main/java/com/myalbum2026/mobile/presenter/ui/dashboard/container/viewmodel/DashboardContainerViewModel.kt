/*
 * DashboardContainerViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.domain.usecase.user.IsInfoShowedUseCase
import com.myalbum2026.mobile.domain.usecase.user.SetIsInfoShowedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardContainerViewModel @Inject constructor(
    private val isInfoShowedUseCase: IsInfoShowedUseCase,
    private val setIsInfoShowedUseCase: SetIsInfoShowedUseCase,
) : ViewModel() {

    private var _dashboardContainerUiEvent = MutableStateFlow<DashboardContainerUiEvent>(DashboardContainerUiEvent.Idle)
    val dashboardContainerUiEvent: StateFlow<DashboardContainerUiEvent> = _dashboardContainerUiEvent.asStateFlow()

    init {
        checkInfoShowed()
    }

    private fun checkInfoShowed() = viewModelScope.launch {
        val isFirstTime = isInfoShowedUseCase().firstOrNull() ?: false
        if (!isFirstTime) {
            _dashboardContainerUiEvent.emit(DashboardContainerUiEvent.ShowInfoDialog)
        }
    }

    fun onAcceptClicked() = viewModelScope.launch {
        setIsInfoShowedUseCase()
    }
}
