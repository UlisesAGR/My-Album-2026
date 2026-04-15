/*
 * CardsObtainedActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.view

import com.myalbum2026.mobile.databinding.ActivityCardsMissingBinding
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsObtainedActivity : BaseOnlyActivity<ActivityCardsMissingBinding>() {

    override fun inflateBinding(): ActivityCardsMissingBinding =
        ActivityCardsMissingBinding.inflate(layoutInflater)

    override fun init() {

    }
}
