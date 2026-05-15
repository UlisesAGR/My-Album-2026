/*
 * CardsUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.cards.viewmodel

sealed class CardsUiEvent {
    data object Idle : CardsUiEvent()
    data class ShowError(val exception: Throwable) : CardsUiEvent()
    data object CardUpdated : CardsUiEvent()
}
