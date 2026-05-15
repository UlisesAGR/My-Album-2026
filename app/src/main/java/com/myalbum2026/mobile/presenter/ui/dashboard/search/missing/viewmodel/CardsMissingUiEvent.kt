/*
 * CardsMissingUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.viewmodel

sealed class CardsMissingUiEvent {
    data object Idle : CardsMissingUiEvent()
    data class ShowError(val exception: Throwable) : CardsMissingUiEvent()
    data object CardUpdated : CardsMissingUiEvent()
}
