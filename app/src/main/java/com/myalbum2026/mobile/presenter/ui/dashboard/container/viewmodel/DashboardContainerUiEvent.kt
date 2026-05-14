/*
 * DashboardContainerUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel

sealed class DashboardContainerUiEvent {
    internal data object Idle : DashboardContainerUiEvent()
    internal data object ShowInfoDialog : DashboardContainerUiEvent()
}
