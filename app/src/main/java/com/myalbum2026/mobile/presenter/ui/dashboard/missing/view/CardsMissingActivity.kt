/*
 * CardsMissingActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view

import android.view.Gravity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsMissingBinding
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.viewmodel.CardsMissingViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsMissingActivity : BaseOnlyActivity<ActivityCardsMissingBinding>() {

    private val cardsMissingViewModel: CardsMissingViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsMissingBinding =
        ActivityCardsMissingBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        flows()
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
            onCardsItemClick = { card ->
                showQuantityDialog(card = card)
            },
        )
    }

    private fun setCardsMissingRecyclerView() {
        binding.cardsMissingRecyclerView.apply {
            setHasFixedSize(true)
            adapter = cardsMissingAdapter
        }
    }

    private fun flows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cardsMissingViewModel.uiState.collect { items ->
                    if (items.isNotEmpty()) {
                        cardsMissingAdapter.updateItems(items)
                    }
                }
            }
        }
    }

    private fun showQuantityDialog(card: CardEntity) {
        QuantityDialog(context = this) { selectedQuantity ->
            cardsMissingViewModel.updateCardQuantity(
                card = card,
                quantity = selectedQuantity,
            )
        }.apply {
            setCancelable(true)
            setTitleCustom(
                getString(
                    R.string.title_quantity_value,
                    card.teamId,
                    card.number,
                )
            )
            setValue(card.quantity)
            show()
        }
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardActivity::class.java,
            finishCurrent = true,
        )
    }
}
