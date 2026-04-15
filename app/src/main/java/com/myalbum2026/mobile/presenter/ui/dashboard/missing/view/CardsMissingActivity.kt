/*
 * CardsMissingActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view

import android.view.Gravity
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityCardsMissingBinding
import com.myalbum2026.mobile.presenter.ui.dashboard.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsMissingActivity : BaseOnlyActivity<ActivityCardsMissingBinding>() {

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsMissingBinding =
        ActivityCardsMissingBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.cardsMissingToolbar,
            title = getString(R.string.cards_missing),
            titleAlignment = Gravity.START,
            iconLeft = R.drawable.ic_arrow_back,
            actionLeftIcon = {
                goToDashboard()
            },
        )
    }

    private fun setCardsMissingAdapter() {
        cardsMissingAdapter = CardsMissingAdapter(
            items = emptyList(),
            onPublicityItemClick = {

            },
            onCardsItemClick = {

            },
        )
    }

    private fun setCardsMissingRecyclerView() {
        binding.cardsMissingRecyclerView.apply {
            setHasFixedSize(true)
            adapter = cardsMissingAdapter
        }
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardActivity::class.java,
            finishCurrent = true,
        )
    }
}
