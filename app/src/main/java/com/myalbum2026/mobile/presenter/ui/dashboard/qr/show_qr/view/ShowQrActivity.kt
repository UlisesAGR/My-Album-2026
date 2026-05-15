/*
 * ShowQrActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityShowQrBinding
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.viewmodel.ShowQrUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.viewmodel.ShowQrViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.backTo
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.setQrCode
import com.myalbum2026.mobile.utils.ui.setScreenBrightness
import com.myalbum2026.mobile.utils.ui.show
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowQrActivity : BaseOnlyActivity<ActivityShowQrBinding>() {

    private val showQrViewModel: ShowQrViewModel by viewModels()

    override fun inflateBinding(): ActivityShowQrBinding =
        ActivityShowQrBinding.inflate(layoutInflater)

    override fun init() {
        setScreenBrightness(brightness = 1.0f)
        setToolbar()
        setEmptyState()
        setOnBackListener()
        setFlows()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.showQrToolbar,
            title = getString(R.string.show_qr),
            titleAlignment = Gravity.START,
            iconLeft = R.drawable.ic_arrow_back,
            actionLeftIcon = {
                onBackPressedDispatcher.onBackPressed()
            },
        )
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(this) {
            backTo()
        }
    }

    private fun setFlows() {
        collect(showQrViewModel.showQrUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            loadQr(qrData = state.qrData)
        }
        collect(showQrViewModel.showQrUiEvent) { state ->
            with(state) {
                when (this) {
                    is ShowQrUiEvent.Idle -> log(message = getString(R.string.idle))
                    is ShowQrUiEvent.ShowError -> toast(message = handleError(exception))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun loadQr(qrData: String?) {
        if (qrData == null) return
        if (qrData.isNotEmpty()) {
            showEmptyState(isEmpty = false)
            binding.qrCodeImageView.setQrCode(data = qrData)
        } else {
            showEmptyState(isEmpty = true)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) = with(binding) {
        if (isEmpty) {
            qrCodeImageView.gone()
            descriptionTextView.gone()
            emptyStateView.root.show()
        } else {
            qrCodeImageView.show()
            descriptionTextView.show()
            emptyStateView.root.gone()
        }
    }

    private fun setEmptyState() = with(binding) {
        emptyStateView.apply {
            titleTextView.text = getString(R.string.no_cards)
            subTitleTextView.text  = getString(R.string.you_don_t_have_a_card_yet)
            retryCustomButton.gone()
        }
    }
}
