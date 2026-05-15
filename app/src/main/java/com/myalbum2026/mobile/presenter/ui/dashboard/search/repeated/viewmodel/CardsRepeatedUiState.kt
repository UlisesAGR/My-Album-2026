/*
 * CardsRepeatedUiState.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.repeated.viewmodel

import com.myalbum2026.mobile.domain.model.CardsItem

data class CardsRepeatedUiState(
    val isLoading: Boolean = false,
    val items: MutableList<CardsItem>? = null,
    val repeatedCards: String? = null,
)
