/*
 * SplashActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.splash.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivitySplashBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.splash.viewmodel.SplashUiEvent
import com.myalbum2026.mobile.presenter.ui.splash.viewmodel.SplashViewModel
import com.myalbum2026.mobile.presenter.ui.welcome.view.WelcomeActivity
import com.myalbum2026.mobile.utils.binding.viewBinding
import com.myalbum2026.mobile.utils.extensions.InAppUpdateManager
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.materialDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySplashBinding::inflate)

    private val splashViewModel: SplashViewModel by viewModels()

    private lateinit var inAppUpdateManager: InAppUpdateManager

    private val updateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                splashViewModel.checkInitialState()
            } else {
                showUpdateRequiredDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        splash.setKeepOnScreenCondition { true }
        setInitUi()
    }

    private fun setInitUi() {
        inAppUpdateManager = InAppUpdateManager(this, updateLauncher)
        setFlows()
        inAppUpdateManager.checkForImmediateUpdate {
            splashViewModel.checkInitialState()
        }
    }

    private fun setFlows() {
        collect(splashViewModel.splashUiEvent) { state ->
            when (state) {
                is SplashUiEvent.Idle -> log(message = getString(R.string.idle))
                is SplashUiEvent.ShowError -> handleError(state.exception)
                is SplashUiEvent.GoToWelcome -> goToWelcome()
                is SplashUiEvent.GoToDashboard -> goToDashboard()
            }
        }
    }

    private fun showUpdateRequiredDialog() {
        materialDialog(
            style = R.style.MaterialDialog,
            isCancelable = false,
            title = getString(R.string.update_required_title),
            message = getString(R.string.update_required_description),
            textPositiveButton = getString(R.string.update_button),
            action = {
                inAppUpdateManager.checkForImmediateUpdate {
                    splashViewModel.checkInitialState()
                }
            },
        )
    }

    private fun goToWelcome() {
        navigateTo(
            destination = WelcomeActivity::class.java,
            finishCurrent = true,
        )
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardActivity::class.java,
            finishCurrent = true,
        )
    }

    override fun onResume() {
        super.onResume()
        inAppUpdateManager.resumeIfNeeded()
    }
}
