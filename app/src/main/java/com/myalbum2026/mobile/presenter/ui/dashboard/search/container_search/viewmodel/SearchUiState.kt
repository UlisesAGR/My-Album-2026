/*
 * SearchUiState.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.viewmodel

import com.myalbum2026.mobile.domain.model.CardsItem

data class SearchUiState(
    val isLoading: Boolean = false,
    val items: MutableList<CardsItem> = mutableListOf(),
)
