/*
 * CardsObtainedActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsObtainedBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel.CardsObtainedUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel.CardsObtainedViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsObtainedActivity : BaseOnlyActivity<ActivityCardsObtainedBinding>() {

    private val cardsObtainedViewModel: CardsObtainedViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsObtainedBinding =
        ActivityCardsObtainedBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setListeners()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        setFlows()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.cardsObtainedToolbar,
            title = getString(R.string.cards_obtained),
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
            onCardItemClick = { card ->
                showQuantityDialog(card = card)
            },
        )
    }

    private fun setCardsMissingRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                when (cardsMissingAdapter.getItemViewType(position)) {
                    0, 1, 2 -> 3
                    3 -> 1
                    else -> 3
                }
        }
        binding.cardsObtainedRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = cardsMissingAdapter
        }
    }

    private fun setFlows() {
        collect(cardsObtainedViewModel.cardsObtainedUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            setItems(items = state.items)
        }
        collect(cardsObtainedViewModel.cardsObtainedUiEvent) { state ->
            with(state) {
                when (this) {
                    is CardsObtainedUiEvent.Idle -> log(message = getString(R.string.idle))
                    is CardsObtainedUiEvent.ShowError -> toast(message = handleError(exception))
                    is CardsObtainedUiEvent.CardUpdated -> log(message = getString(R.string.idle))
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
            cardsObtainedViewModel.updateCardQuantity(
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
