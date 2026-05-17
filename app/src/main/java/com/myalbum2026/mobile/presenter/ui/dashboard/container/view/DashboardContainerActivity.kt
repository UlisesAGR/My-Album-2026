/*
 * DashboardContainerActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.view

import androidx.activity.addCallback
import androidx.activity.viewModels
import com.google.android.gms.ads.AdRequest
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityDashboardContainerBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardContainerUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardContainerViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.home.view.HomeFragment
import com.myalbum2026.mobile.presenter.ui.dashboard.qr.container_qr.ContainerQrFragment
import com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.view.SearchFragment
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.logger.logInfo
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.materialDialog
import com.myalbum2026.mobile.utils.ui.replaceFragment
import com.myalbum2026.mobile.utils.ui.setAnimationStart
import com.myalbum2026.mobile.utils.ui.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardContainerActivity : BaseOnlyActivity<ActivityDashboardContainerBinding>() {

    private val dashboardContainerViewModel: DashboardContainerViewModel by viewModels()

    override fun inflateBinding(): ActivityDashboardContainerBinding =
        ActivityDashboardContainerBinding.inflate(layoutInflater)

    override fun init() {
        setBanner()
        setInitViewInBottomNavigation()
        setListeners()
        setFlows()
    }

    private fun setListeners() {
        setOnBackListener()
        setBottomSheetListener()
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

    private fun setBottomSheetListener() = with(binding) {
        adminBottomNavigation.setOnItemSelectedListener { itemMenu ->
            if (itemMenu.itemId == adminBottomNavigation.selectedItemId) {
                return@setOnItemSelectedListener true
            }
            when (itemMenu.itemId) {
                R.id.home -> {
                    adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
                    replaceFragment(adminFrameLayout, fragment = HomeFragment())
                    true
                }
                R.id.search -> {
                    adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
                    replaceFragment(adminFrameLayout, fragment = SearchFragment())
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

    private fun setBanner() = with(binding) {
        val shouldShowAds = resources.getBoolean(R.bool.show_ads)
        if (shouldShowAds) {
            bannerPublicity.apply {
                loadAd(AdRequest.Builder().build())
                show()
            }
        } else {
            bannerPublicity.gone()
        }
    }

    private fun setInitViewInBottomNavigation() = with(binding) {
        adminFrameLayout.setAnimationStart(animationId = R.anim.fade_in)
        replaceFragment(adminFrameLayout, fragment = HomeFragment())
    }
}
