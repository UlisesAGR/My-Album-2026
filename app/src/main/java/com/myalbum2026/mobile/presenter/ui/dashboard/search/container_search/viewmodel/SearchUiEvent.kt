/*
 * SearchUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.viewmodel

sealed class SearchUiEvent {
    internal data object Idle : SearchUiEvent()
    data class ShowError(val exception: Throwable) : SearchUiEvent()
}
