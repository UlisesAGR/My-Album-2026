/*
 * CardsRepeatedUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.repeated.viewmodel

sealed class CardsRepeatedUiEvent {
    data object Idle : CardsRepeatedUiEvent()
    data class ShowError(val exception: Throwable) : CardsRepeatedUiEvent()
    data object CardUpdated : CardsRepeatedUiEvent()
}
