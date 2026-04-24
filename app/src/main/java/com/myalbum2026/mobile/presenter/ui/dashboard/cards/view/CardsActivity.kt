/*
 * CardsActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.cards.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ActivityCardsBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.dialog.quantity.QuantityBottomSheet
import com.myalbum2026.mobile.presenter.ui.dashboard.cards.viewmodel.CardsUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.cards.viewmodel.CardsViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.countries.view.CountryListActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.CardsMissingAdapter
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_TEAM_ID
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getSerializable
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.extensions.shareText
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.setVisibility
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsActivity : BaseOnlyActivity<ActivityCardsBinding>() {

    private val cardsViewModel: CardsViewModel by viewModels()

    private lateinit var cardsMissingAdapter: CardsMissingAdapter

    private lateinit var cardType: CardType

    override fun inflateBinding(): ActivityCardsBinding =
        ActivityCardsBinding.inflate(layoutInflater)

    override fun init() {
        cardType = intent.getSerializable<CardType>(EXTRA_CARD_TYPE) ?: CardType.MISSING
        val teamId = intent.getStringExtra(EXTRA_TEAM_ID).orEmpty()

        setToolbar()
        validateFabShareVisibility()
        setListeners()
        setCardsMissingAdapter()
        setCardsMissingRecyclerView()
        setFlows()

        cardsViewModel.getFullAlbum(
            cardType = cardType,
            teamId = teamId,
        )
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.cardsToolbar,
            title = getString(if (cardType == CardType.MISSING) R.string.cards_missing else R.string.cards_obtained),
            titleAlignment = Gravity.START,
            iconLeft = R.drawable.ic_arrow_back,
            actionLeftIcon = {
                goToCountryListActivity()
            },
        )
    }

    private fun validateFabShareVisibility() {
        binding.fabShareMissing.setVisibility(state = cardType == CardType.MISSING)
    }

    private fun setListeners() {
        binding.fabShareMissing.setOnClickListener {
            handleShareAction()
        }
        setOnBackListener()
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(owner = this) {
            goToCountryListActivity()
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
        binding.cardsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = cardsMissingAdapter
        }
    }

    private fun setFlows() {
        collect(cardsViewModel.cardsUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            setItems(items = state.items)
        }
        collect(cardsViewModel.cardsUiEvent) { state ->
            with(state) {
                when (this) {
                    is CardsUiEvent.Idle -> log(message = getString(R.string.idle))
                    is CardsUiEvent.ShowError -> toast(message = handleError(exception))
                    is CardsUiEvent.CardUpdated -> log(message = getString(R.string.idle))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun setItems(items: MutableList<CardsItem>) {
        if (items.isNotEmpty()) {
            cardsMissingAdapter.updateItems(items = items)
        }
    }

    private fun handleShareAction() {
        val message = cardsViewModel.getMissingCardsFormattedText()
        if (message.isNotEmpty()) {
            shareText(
                title = getString(R.string.share_with),
                message = message,
                onError = {
                    toast(message = getString(R.string.error_share_missing_cards))
                },
            )
        } else {
            toast(message = getString(R.string.no_missing_cards))
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
                cardsViewModel.updateCardQuantity(card, selectedQuantity)
            },
        ).show(supportFragmentManager, QuantityBottomSheet.TAG)
    }

    private fun goToCountryListActivity() {
        navigateTo(
            destination = CountryListActivity::class.java,
            finishCurrent = true,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, cardType)
            }
        )
    }
}
