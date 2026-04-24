/*
 * CountryListActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.countries.view

import android.view.Gravity
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ActivityCountryListBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.cards.view.CardsActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.container.view.DashboardActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.countries.view.adapter.CountryListAdapter
import com.myalbum2026.mobile.presenter.ui.dashboard.countries.viewmodel.CountryListUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.countries.viewmodel.CountryListViewModel
import com.myalbum2026.mobile.utils.base.BaseOnlyActivity
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_TEAM_ID
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getSerializable
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
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
                goToDashboard()
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
            goToDashboard()
        }
    }

    private fun setCountriesAdapter() {
        countryListAdapter = CountryListAdapter(
            onTeamSelected = { team ->
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
            countryListAdapter.updateList(newList = items)
            showEmptyState(isEmpty = false)
        } else {
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
            titleTextView.text = getString(R.string.complete_cards)
            subTitleTextView.text  = getString(R.string.congratulations_you_completed_your_album)
            retryCustomButton.gone()
        }
    }

    private fun goToDashboard() {
        navigateTo(
            destination = DashboardActivity::class.java,
            finishCurrent = true,
        )
    }

    private fun goToCards(id: String) {
        navigateTo(
            destination = CardsActivity::class.java,
            finishCurrent = true,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, cardType)
                putExtra(EXTRA_TEAM_ID, id)
            }
        )
    }
}
