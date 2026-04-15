/*
 * DashboardActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard

import com.myalbum2026.mobile.databinding.ActivityDashboardBinding
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseOnlyActivity<ActivityDashboardBinding>() {

    override fun inflateBinding(): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(layoutInflater)

    override fun init() {
        setListeners()
    }

    private fun setListeners() {

    }
}
