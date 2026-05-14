/*
 * ShowQrUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.viewmodel

sealed class ShowQrUiEvent {
    data object Idle : ShowQrUiEvent()
    data class ShowError(val exception: Throwable) : ShowQrUiEvent()
}
