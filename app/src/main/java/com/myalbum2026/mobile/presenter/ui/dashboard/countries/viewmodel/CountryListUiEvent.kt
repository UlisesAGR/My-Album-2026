/*
 * CountryListUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.countries.viewmodel

sealed class CountryListUiEvent {
    data object Idle : CountryListUiEvent()
    data class ShowError(val exception: Throwable) : CountryListUiEvent()
}
