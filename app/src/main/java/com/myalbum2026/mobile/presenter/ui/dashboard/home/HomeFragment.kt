/*
 * HomeFragment.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.FragmentHomeBinding
import com.myalbum2026.mobile.utils.base.BaseFragment
import com.myalbum2026.mobile.utils.extensions.getVersionName
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        setText()
    }

    private fun setText() = with(binding)  {
        versionTextView.text = getString(
            R.string.version_value,
            requireActivity().getVersionName(),
        )
    }
}
