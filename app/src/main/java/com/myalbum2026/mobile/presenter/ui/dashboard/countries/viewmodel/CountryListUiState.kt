/*
 * CountryListUiState.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.countries.viewmodel

import com.myalbum2026.mobile.domain.model.CardsItem

data class CountryListUiState(
    val isLoading: Boolean = false,
    val teams: List<CardsItem.TeamHeader> = emptyList(),
)
