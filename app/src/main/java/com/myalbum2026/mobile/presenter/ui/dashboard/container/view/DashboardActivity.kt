/*
 * DashboardActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.container.view

import android.view.Gravity
import androidx.activity.viewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityDashboardBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.container.viewmodel.DashboardViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.CardsMissingActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.obtained.view.CardsObtainedActivity
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getVersionName
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.ui.materialDialog
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
        flows()
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

    private fun flows() {
        collect(dashboardViewModel.uiState) { items ->
            val progress = items.filterIsInstance<CardsMissingItem.Progress>().firstOrNull()
            progress?.let { progress ->
                updateProgressUI(progress = progress)
            }
        }
        collect(dashboardViewModel.dashboardUiEvent) { state ->
            when (state) {
                is DashboardUiEvent.Idle -> log(message = getString(R.string.idle))
                is DashboardUiEvent.ShowInfoDialog -> showInfoDialog()
            }
        }
    }

    private fun updateProgressUI(progress: CardsMissingItem.Progress) = with(binding) {
        myProgressObtainedTextView.text = getString(
            R.string.progress_obtained_format,
            progress.obtained,
            progress.total,
        )
        myProgressMissingTextView.text = getString(
            R.string.progress_missing_format,
            progress.missing,
        )
    }

    private fun showInfoDialog() {
        materialDialog(
            style = R.style.MaterialDialog,
            isCancelable = false,
            title = getString(R.string.important),
            textPositiveButton = getString(R.string.accept),
            message = getString(R.string.if_you_delete_your_app_you_will_lose_your_album_progress),
            action = {
                dashboardViewModel.onAcceptClicked()
            },
        )
    }

    private fun goToCardsObtained() {
        navigateTo(
            destination = CardsObtainedActivity::class.java,
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
