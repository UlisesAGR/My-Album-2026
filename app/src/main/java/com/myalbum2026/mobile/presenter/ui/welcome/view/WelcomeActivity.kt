package com.myalbum2026.mobile.presenter.ui.welcome.view

import androidx.activity.viewModels
import com.myalbum2026.mobile.databinding.ActivityWelcomeBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.welcome.viewmodel.WelcomeViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseOnlyActivity<ActivityWelcomeBinding>() {

    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun inflateBinding(): ActivityWelcomeBinding =
        ActivityWelcomeBinding.inflate(layoutInflater)

    override fun init() {
        setListeners()
    }

    private fun setListeners() {
        binding.startCustomButton.setOnClickListener {
            welcomeViewModel.onContinueClicked()
            goToDashboard()
        }
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardActivity::class.java,
            finishCurrent = true,
        )
    }
}