/*
 * WelcomeViewModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myalbum2026.mobile.domain.usecase.user.SetFirstTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val setFirstTimeUseCase: SetFirstTimeUseCase,
): ViewModel() {

    fun onContinueClicked() = viewModelScope.launch {
        setFirstTimeUseCase()
    }
}
