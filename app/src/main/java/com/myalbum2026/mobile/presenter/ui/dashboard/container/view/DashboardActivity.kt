/*
 * DashboardActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.view

import android.view.Gravity
import androidx.activity.viewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityDashboardBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.CardsMissingActivity
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.getVersionName
import com.myalbum2026.mobile.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseOnlyActivity<ActivityDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun inflateBinding(): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setText()
        setListeners()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.dashboardToolbar,
            title = getString(R.string.app_name),
            titleAlignment = Gravity.CENTER,
        )
    }

    private fun setText() = with(binding)  {
        versionTextView.text = getString(
            R.string.version_value,
            getVersionName(),
        )
    }

    private fun setListeners() = with(binding) {
        cardsObtainedCustomButton.setOnClickListener {
            goToCardsObtained()
        }
        cardsMissingCustomButton.setOnClickListener {
            goToCardsMissing()
        }
    }

    private fun goToCardsObtained() {
        navigateTo(
            destination = CardsMissingActivity::class.java,
            finishCurrent = true,
        )
    }

    private fun goToCardsMissing() {
        navigateTo(
            destination = CardsMissingActivity::class.java,
            finishCurrent = true,
        )
    }
}
