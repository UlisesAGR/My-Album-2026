/*
 * ScanQrUiEvent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.viewmodel

import com.myalbum2026.mobile.data.model.CardEntity

sealed class ScanQrUiEvent {
    data object Idle : ScanQrUiEvent()
    data class ShowError(val exception: Throwable) : ScanQrUiEvent()
    data class ComparisonSuccess(val matchCards: List<CardEntity>) : ScanQrUiEvent()
}
