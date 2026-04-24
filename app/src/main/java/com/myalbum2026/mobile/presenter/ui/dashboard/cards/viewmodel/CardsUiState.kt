/*
 * CardsUiState.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.cards.viewmodel

import com.myalbum2026.mobile.domain.model.CardsItem

data class CardsUiState(
    val isLoading: Boolean = false,
    val items: MutableList<CardsItem>? = null,
)
