/*
 * CardsObtainedActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.obtained.view

import android.view.Gravity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsObtainedBinding
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.obtained.viewmodel.CardsObtainedViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsObtainedActivity : BaseOnlyActivity<ActivityCardsObtainedBinding>() {

    private val cardsObtainedViewModel: CardsObtainedViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsObtainedBinding =
        ActivityCardsObtainedBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        flows()
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

    private fun setCardsMissingAdapter() {
        cardsMissingAdapter = CardsMissingAdapter(
            onCardsItemClick = { card ->
                showQuantityDialog(card = card)
            },
        )
    }

    private fun setCardsMissingRecyclerView() {
        binding.cardsObtainedRecyclerView.apply {
            setHasFixedSize(true)
            adapter = cardsMissingAdapter
        }
    }

    private fun flows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cardsObtainedViewModel.uiState.collect { items ->
                    if (items.isNotEmpty()) {
                        cardsMissingAdapter.updateItems(items)
                    }
                }
            }
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
