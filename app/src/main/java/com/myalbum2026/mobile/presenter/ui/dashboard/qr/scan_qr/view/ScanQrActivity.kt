/*
 * ScanQrActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.view

import android.Manifest
import android.content.pm.PackageManager
import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityScanQrBinding
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.viewmodel.ScanQrUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.scan_qr.viewmodel.ScanQrViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.Constants.SEPARATOR
import com.myalbum2026.mobile.utils.extensions.backTo
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.materialDialog
import com.myalbum2026.mobile.utils.ui.setupScanner
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanQrActivity : BaseOnlyActivity<ActivityScanQrBinding>() {

    private val scanQrViewModel: ScanQrViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            binding.barcodeScanner.resume()
        } else {
            toast(message = getString(R.string.camera_permission_is_required_to_scan))
            backTo()
        }
    }

    override fun inflateBinding(): ActivityScanQrBinding =
        ActivityScanQrBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setOnBackListener()
        checkCameraPermission()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.scanQrToolbar,
            title = getString(R.string.scan_qr),
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

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startScanner()
                setFlows()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startScanner() {
        binding.barcodeScanner.apply {
            statusView.gone()
            setupScanner { qrData ->
                scanQrViewModel.processQrData(qrData = qrData)
            }
        }
    }

    private fun setFlows() {
        collect(scanQrViewModel.scanQrUiState) { state ->
            statusLoading(isLoading = state.isLoading)
        }
        collect(scanQrViewModel.scanQrUiEvent) { state ->
            with(state) {
                when (this) {
                    is ScanQrUiEvent.Idle -> log(message = getString(R.string.idle))
                    is ScanQrUiEvent.ShowError -> toast(message = handleError(exception))
                    is ScanQrUiEvent.ComparisonSuccess -> showComparisonSuccess(cards = matchCards)
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun showComparisonSuccess(cards: List<CardEntity>) {
        val message = buildString {
            append(getString(R.string.cards_that_are_useful_to_you))
            append(cards.joinToString(SEPARATOR) { card -> card.id })
        }
        materialDialog(
            style = R.style.MaterialDialogStyle,
            title = getString(R.string.exchange_found),
            message = message,
            textPositiveButton = getString(R.string.accept),
            textNegativeButton = getString(R.string.cancel),
        )
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }
}
