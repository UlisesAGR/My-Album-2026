/*
 * WelcomeActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.welcome

import com.myalbum2026.mobile.databinding.ActivityWelcomeBinding
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseOnlyActivity<ActivityWelcomeBinding>() {

    override fun inflateBinding(): ActivityWelcomeBinding =
        ActivityWelcomeBinding.inflate(layoutInflater)

    override fun init() {

    }
}
