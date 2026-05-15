/*
 * CountryListActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityCountryListBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.search.cards.view.CardsActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.view.adapter.CountryListAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.viewmodel.CountryListUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.viewmodel.CountryListViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_TEAM_ID
import com.myalbum2026.mobile.utils.extensions.backTo
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getSerializable
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.hideKeyboard
import com.myalbum2026.mobile.utils.ui.show
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountryListActivity : BaseOnlyActivity<ActivityCountryListBinding>() {

    private val countryListViewModel: CountryListViewModel by viewModels()

    private lateinit var countryListAdapter: CountryListAdapter

    private lateinit var cardType: CardType

    override fun inflateBinding(): ActivityCountryListBinding =
        ActivityCountryListBinding.inflate(layoutInflater)

    override fun init() {
        cardType = intent.getSerializable<CardType>(EXTRA_CARD_TYPE) ?: CardType.MISSING

        setToolbar()
        setEmptyState()
        setListeners()
        setCountriesAdapter()
        setCountriesRecyclerView()
        setFlows()

        countryListViewModel.getAllTeams(cardType = cardType)
    }

    private fun setToolbar() {
        setupAppBar(
            toolbar = binding.countyListToolbar,
            title = getString(if (cardType == CardType.MISSING) R.string.cards_missing else R.string.cards_obtained),
            titleAlignment = Gravity.START,
            iconLeft = R.drawable.ic_arrow_back,
            actionLeftIcon = {
                onBackPressedDispatcher.onBackPressed()
            },
        )
    }

    private fun setListeners() {
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            countryListAdapter.filter.filter(text)
        }
        setOnBackListener()
    }

    private fun setOnBackListener() {
        onBackPressedDispatcher.addCallback(owner = this) {
            backTo()
        }
    }

    private fun setCountriesAdapter() {
        countryListAdapter = CountryListAdapter(
            onTeamSelected = { team ->
                resetFinderToNavigate()
                goToCards(id = team.id)
            },
        )
    }

    private fun setCountriesRecyclerView() {
        binding.countyRecyclerView.apply {
            setHasFixedSize(true)
            adapter = countryListAdapter
        }
    }

    private fun setFlows() {
        collect(countryListViewModel.countryListUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            setItems(items = state.teams)
        }
        collect(countryListViewModel.countryListUiEvent) { state ->
            with(state) {
                when (this) {
                    is CountryListUiEvent.Idle -> log(message = getString(R.string.idle))
                    is CountryListUiEvent.ShowError -> toast(message = handleError(exception))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(supportFragmentManager)
        else LoadingDialog.dismiss(supportFragmentManager)
    }

    private fun setItems(items: List<CardsItem.TeamHeader>?) {
        if (items == null) return
        if (items.isNotEmpty()) {
            countryListAdapter.updateData(items)
            showEmptyState(isEmpty = false)
        } else {
            countryListAdapter.updateData(emptyList())
            showEmptyState(isEmpty = true)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) = with(binding) {
        if (isEmpty) {
            countyRecyclerView.gone()
            emptyStateView.root.show()
            searchBox.gone()
        } else {
            countyRecyclerView.show()
            emptyStateView.root.gone()
            searchBox.show()
        }
    }

    private fun setEmptyState() = with(binding) {
        emptyStateView.apply {
            titleTextView.text = getString(R.string.no_cards)
            subTitleTextView.text  = getString(R.string.you_don_t_have_a_card_yet)
            retryCustomButton.gone()
        }
    }

    private fun resetFinderToNavigate() = with(binding) {
        root.hideKeyboard()
        searchEditText.apply {
            clearFocus()
            setText("")
        }
    }

    private fun goToCards(id: String) {
        navigateTo(
            destination = CardsActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, cardType)
                putExtra(EXTRA_TEAM_ID, id)
            },
        )
    }
}
