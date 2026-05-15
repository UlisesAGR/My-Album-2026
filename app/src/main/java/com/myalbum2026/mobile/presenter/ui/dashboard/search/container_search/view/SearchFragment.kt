/*
 * SearchFragment.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.FragmentSearchBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.view.CountryListActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.viewmodel.SearchUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.search.container_search.viewmodel.SearchViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.CardsMissingActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.search.obtained.view.CardsObtainedActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.search.repeated.view.CardsRepeatedActivity
import com.myalbum2026.mobile.utils.base.BaseFragment
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getVersionName
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    override fun init() {
        setText()
        setListeners()
        setFlows()
    }

    private fun setText() = with(binding)  {
        versionTextView.text = getString(
            R.string.version_value,
            requireActivity().getVersionName(),
        )
    }

    private fun setListeners() = with(binding) {
        cardsObtainedCustomButton.setOnClickListener {
            goToCardsObtained()
        }
        cardsMissingCustomButton.setOnClickListener {
            goToCardsMissing()
        }
        countyObtainedCustomButton.setOnClickListener {
            goToCountryObtained()
        }
        countyMissingCustomButton.setOnClickListener {
            goToCountryMissing()
        }
        repeatedCustomButton.setOnClickListener {
            goToCardsRepeated()
        }
    }

    private fun setFlows() {
        collect(searchViewModel.searchUiState) { state ->
            statusLoading(isLoading = state.isLoading)
        }
        collect(searchViewModel.searchUiEvent) { state ->
            with(state) {
                when (this) {
                    is SearchUiEvent.Idle -> log(message = getString(R.string.idle))
                    is SearchUiEvent.ShowError -> requireContext().toast(message = requireContext().handleError(exception))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(childFragmentManager)
        else LoadingDialog.dismiss(childFragmentManager)
    }

    private fun goToCardsObtained() {
        requireActivity().navigateTo(
            destination = CardsObtainedActivity::class.java,
        )
    }

    private fun goToCardsMissing() {
        requireActivity().navigateTo(
            destination = CardsMissingActivity::class.java,
        )
    }

    private fun goToCountryObtained() {
        requireActivity().navigateTo(
            destination = CountryListActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.OBTAINED)
            },
        )
    }

    private fun goToCountryMissing() {
        requireActivity().navigateTo(
            destination = CountryListActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }

    private fun goToCardsRepeated() {
        requireActivity().navigateTo(
            destination = CardsRepeatedActivity::class.java,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }
}
