/*
 * ContainerQrFragment
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.container_qr

import android.view.LayoutInflater
import android.view.ViewGroup
import com.myalbum2026.mobile.databinding.FragmentContainerQrBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.view.ScanQrActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.show_qr.view.ShowQrActivity
import com.myalbum2026.mobile.utils.base.BaseFragment
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.navigateTo

class ContainerQrFragment : BaseFragment<FragmentContainerQrBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentContainerQrBinding =
        FragmentContainerQrBinding.inflate(layoutInflater)

    override fun init() {
        setListeners()
    }

    private fun setListeners() = with(binding) {
        generateQrCustomButton.setOnClickListener {
            goToGenerateQrRepeated()
        }
        scanQrCustomButton.setOnClickListener {
            goToScanQrRepeated()
        }
    }

    private fun goToGenerateQrRepeated() {
        requireActivity().navigateTo(
            destination = ShowQrActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }

    private fun goToScanQrRepeated() {
        requireActivity().navigateTo(
            destination = ScanQrActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }
}
