/*
 * CardsMissingUiState.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel

import com.myalbum2026.mobile.domain.model.CardsMissingItem

data class CardsObtainedUiState(
    val isLoading: Boolean = false,
    val items: MutableList<CardsMissingItem> = mutableListOf(),
)
