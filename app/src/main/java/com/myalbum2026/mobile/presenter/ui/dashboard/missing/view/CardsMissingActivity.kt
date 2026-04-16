/*
 * CardsMissingActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsMissingBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.viewmodel.CardsMissingUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.viewmodel.CardsMissingViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsMissingActivity : BaseOnlyActivity<ActivityCardsMissingBinding>() {

    private val cardsMissingViewModel: CardsMissingViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsMissingBinding =
        ActivityCardsMissingBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setListeners()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        setFlows()
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

    private fun setListeners() {
        setOnBackListener()
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(owner = this) {
            goToDashboard()
        }
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

    private fun setFlows() {
        collect(cardsMissingViewModel.cardsMissingUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            setItems(items = state.items)
        }
        collect(cardsMissingViewModel.cardsMissingUiEvent) { state ->
            with(state) {
                when (this) {
                    is CardsMissingUiEvent.Idle -> log(message = getString(R.string.idle))
                    is CardsMissingUiEvent.ShowError -> toast(message = handleError(exception))
                    is CardsMissingUiEvent.CardUpdated -> log(message = getString(R.string.idle))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun setItems(items: MutableList<CardsMissingItem>) {
        if (items.isNotEmpty()) {
            cardsMissingAdapter.updateItems(items = items)
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
