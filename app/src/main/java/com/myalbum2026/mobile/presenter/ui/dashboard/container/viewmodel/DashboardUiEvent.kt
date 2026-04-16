/*
 * DashboardUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel

sealed class DashboardUiEvent {
    internal data object Idle : DashboardUiEvent()
    internal data object ShowInfoDialog : DashboardUiEvent()
}
