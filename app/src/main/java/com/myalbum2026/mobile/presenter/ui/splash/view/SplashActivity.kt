/*
 * SplashActivity.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.splash.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivitySplashBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.splash.viewmodel.SplashUiEvent
import com.myalbum2026.mobile.presenter.ui.splash.viewmodel.SplashViewModel
import com.myalbum2026.mobile.presenter.ui.welcome.WelcomeActivity
import com.myalbum2026.mobile.utils.binding.viewBinding
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySplashBinding::inflate)

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        splash.setKeepOnScreenCondition { true }
        setInitUi()
    }

    private fun setInitUi() {
        setFlows()
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
}
