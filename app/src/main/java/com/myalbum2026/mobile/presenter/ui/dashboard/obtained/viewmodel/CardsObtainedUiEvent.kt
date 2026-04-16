/*
 * CardsObtainedUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel

sealed class CardsObtainedUiEvent {
    data object Idle : CardsObtainedUiEvent()
    data class ShowError(val exception: Throwable) : CardsObtainedUiEvent()
    data object CardUpdated : CardsObtainedUiEvent()
}
