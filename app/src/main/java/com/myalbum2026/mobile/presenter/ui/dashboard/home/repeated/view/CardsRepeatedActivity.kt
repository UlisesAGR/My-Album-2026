/*
 * CardsRepeatedActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.repeated.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsRepeatedBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityBottomSheet
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardContainerActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.home.repeated.viewmodel.CardsRepeatedUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.home.repeated.viewmodel.CardsRepeatedViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.extensions.shareText
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.show
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsRepeatedActivity : BaseOnlyActivity<ActivityCardsRepeatedBinding>() {

    private val cardsRepeatedViewModel: CardsRepeatedViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    override fun inflateBinding(): ActivityCardsRepeatedBinding =
        ActivityCardsRepeatedBinding.inflate(layoutInflater)

    override fun init() {
        setToolbar()
        setEmptyState()
        setListeners()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        setFlows()
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.cardsRepeatedToolbar,
            title = getString(R.string.cards_repeated),
            titleAlignment = Gravity.START,
            iconLeft = R.drawable.ic_arrow_back,
            actionLeftIcon = {
                goToDashboard()
            },
        )
    }

    private fun setListeners() {
        binding.fabShareRepeated.setOnClickListener {
            cardsRepeatedViewModel.getRepeatedCards()
        }
        setOnBackListener()
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(owner = this) {
            goToDashboard()
        }
    }

    private fun setCardsMissingAdapter() {
        cardsMissingAdapter = CardsMissingAdapter(
            cardType = CardType.REPEATED,
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
        binding.cardsRepeatedRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = cardsMissingAdapter
        }
    }

    private fun setFlows() {
        collect(cardsRepeatedViewModel.cardsRepeatedUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            setItems(items = state.items)
            handleShareAction(repeatedCards = state.repeatedCards)
        }
        collect(cardsRepeatedViewModel.cardsRepeatedUiEvent) { state ->
            with(state) {
                when (this) {
                    is CardsRepeatedUiEvent.Idle -> log(message = getString(R.string.idle))
                    is CardsRepeatedUiEvent.ShowError -> toast(message = handleError(exception))
                    is CardsRepeatedUiEvent.CardUpdated -> log(message = getString(R.string.idle))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun setItems(items: MutableList<CardsItem>?) {
        if (items == null) return
        if (items.isNotEmpty()) {
            cardsMissingAdapter.submitList(items)
            showEmptyState(isEmpty = false)
        } else {
            cardsMissingAdapter.submitList(emptyList())
            showEmptyState(isEmpty = true)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) = with(binding) {
        if (isEmpty) {
            cardsRepeatedRecyclerView.gone()
            emptyStateView.root.show()
            fabShareRepeated.hide()
        } else {
            cardsRepeatedRecyclerView.show()
            emptyStateView.root.gone()
            fabShareRepeated.show()
        }
    }

    private fun setEmptyState() = with(binding) {
        emptyStateView.apply {
            titleTextView.text = getString(R.string.cards_repeated)
            subTitleTextView.text  = getString(R.string.no_repeated_cards)
            retryCustomButton.gone()
        }
    }

    private fun handleShareAction(repeatedCards: String?) {
        if (repeatedCards == null) return
        if (repeatedCards.isNotEmpty()) {
            shareText(
                title = getString(R.string.share_with_repeated),
                message = repeatedCards,
                onError = {
                    toast(message = getString(R.string.error_share_reapeated_cards))
                },
            )
        } else {
            toast(message = getString(R.string.no_repeated_cards))
        }
    }

    private fun showQuantityDialog(card: CardEntity) {
        val title = getString(
            R.string.title_quantity_value,
            card.teamId,
            card.number,
        )
        QuantityBottomSheet(
            initialQuantity = card.quantity,
            title = title,
            onConfirm = { selectedQuantity ->
                cardsRepeatedViewModel.updateCardQuantity(card, selectedQuantity)
            },
        ).show(supportFragmentManager, QuantityBottomSheet.TAG)
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardContainerActivity::class.java,
            finishCurrent = true,
        )
    }
}
