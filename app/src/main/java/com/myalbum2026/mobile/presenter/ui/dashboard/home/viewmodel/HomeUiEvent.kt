/*
 * HomeUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.viewmodel

sealed class HomeUiEvent {
    internal data object Idle : HomeUiEvent()
    data class ShowError(val exception: Throwable) : HomeUiEvent()
}
