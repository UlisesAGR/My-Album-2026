/*
 * DashboardContainerActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.view

import androidx.activity.addCallback
import androidx.activity.viewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityDashboardContainerBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardContainerUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardContainerViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.home.home.view.HomeFragment
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.container_qr.ContainerQrFragment
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.logger.logInfo
import com.myalbum2026.mobile.utils.ui.materialDialog
import com.myalbum2026.mobile.utils.ui.replaceFragment
import com.myalbum2026.mobile.utils.ui.setAnimationStart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardContainerActivity : BaseOnlyActivity<ActivityDashboardContainerBinding>() {

    private val dashboardContainerViewModel: DashboardContainerViewModel by viewModels()

    override fun inflateBinding(): ActivityDashboardContainerBinding =
        ActivityDashboardContainerBinding.inflate(layoutInflater)

    override fun init() {
        setBottomSheet()
        setListeners()
        setFlows()
    }

    private fun setListeners() {
        setOnBackListener()
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(owner = this) {
            logInfo(getString(R.string.back))
        }
    }

    private fun setFlows() {
        collect(dashboardContainerViewModel.dashboardContainerUiEvent) { state ->
            with(state) {
                when (this) {
                    is DashboardContainerUiEvent.Idle -> log(message = getString(R.string.idle))
                    is DashboardContainerUiEvent.ShowInfoDialog -> showInfoDialog()
                }
            }
        }
    }

    private fun showInfoDialog() {
        materialDialog(
            style = R.style.MaterialDialogStyle,
            isCancelable = false,
            title = getString(R.string.important),
            textPositiveButton = getString(R.string.accept),
            message = getString(R.string.if_you_delete_your_app_you_will_lose_your_album_progress),
            action = {
                dashboardContainerViewModel.onAcceptClicked()
            },
        )
    }

    private fun setBottomSheet() = with(binding) {
        adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
        replaceFragment(adminFrameLayout, fragment = HomeFragment())
        adminBottomNavigation.setOnItemSelectedListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.home -> {
                    adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
                    replaceFragment(adminFrameLayout, fragment = HomeFragment())
                    true
                }
                R.id.qr -> {
                    adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
                    replaceFragment(adminFrameLayout, fragment = ContainerQrFragment())
                    true
                }
                else -> false
            }
        }
    }
}
