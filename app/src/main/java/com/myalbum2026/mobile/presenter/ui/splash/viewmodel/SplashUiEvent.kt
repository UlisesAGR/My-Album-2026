/*
 * SplashUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.splash.viewmodel

sealed class SplashUiEvent {
    internal data object Idle : SplashUiEvent()
    internal data class ShowError(val exception: Throwable) : SplashUiEvent()
    internal data object GoToWelcome : SplashUiEvent()
    internal data object GoToDashboard : SplashUiEvent()
}
