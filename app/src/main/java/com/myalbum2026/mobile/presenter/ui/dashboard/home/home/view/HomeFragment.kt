/*
 * HomeFragment.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.gms.ads.AdRequest
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.FragmentHomeBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.home.countries.view.CountryListActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.home.home.viewmodel.DashboardUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.home.home.viewmodel.HomeViewModel
import com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.CardsMissingActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.home.obtained.view.CardsObtainedActivity
import com.myalbum2026.mobile.presenter.ui.dashboard.home.repeated.view.CardsRepeatedActivity
import com.myalbum2026.mobile.utils.base.BaseFragment
import com.myalbum2026.mobile.utils.extensions.Constants.EXTRA_CARD_TYPE
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getVersionName
import com.myalbum2026.mobile.utils.extensions.navigateTo
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.show
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        setText()
        setBanner()
        setListeners()
        setFlows()
    }

    private fun setText() = with(binding)  {
        versionTextView.text = getString(
            R.string.version_value,
            requireActivity().getVersionName(),
        )
    }

    private fun setBanner() = with(binding) {
        val shouldShowAds = resources.getBoolean(R.bool.show_ads)
        if (shouldShowAds) {
            bannerPublicity.apply {
                loadAd(AdRequest.Builder().build())
                show()
            }
        } else {
            bannerPublicity.gone()
        }
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
        collect(homeViewModel.homeUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            updateProgress(items = state.items)
        }
        collect(homeViewModel.homeUiEvent) { state ->
            with(state) {
                when (this) {
                    is DashboardUiEvent.Idle -> log(message = getString(R.string.idle))
                    is DashboardUiEvent.ShowError -> requireContext().toast(message = requireContext().handleError(exception))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(childFragmentManager)
        else LoadingDialog.dismiss(childFragmentManager)
    }

    private fun updateProgress(
        items: MutableList<CardsItem>,
    ) = with(binding) {
        val progress = items.filterIsInstance<CardsItem.Progress>().firstOrNull()
        progress?.let { progress ->
            myProgressObtainedTextView.text = getString(
                R.string.progress_obtained_format,
                progress.obtained,
                progress.total,
            )
            myProgressMissingTextView.text = getString(
                R.string.progress_missing_format,
                progress.missing,
            )
            myProgressRepeatedTextView.text = getString(
                R.string.progress_repeated_format,
                progress.repeated,
            )
        }
    }

    private fun goToCardsObtained() {
        requireActivity().navigateTo(
            destination = CardsObtainedActivity::class.java,
            finishCurrent = true,
        )
    }

    private fun goToCardsMissing() {
        requireActivity().navigateTo(
            destination = CardsMissingActivity::class.java,
            finishCurrent = true,
        )
    }

    private fun goToCountryObtained() {
        requireActivity().navigateTo(
            destination = CountryListActivity::class.java,
            finishCurrent = true,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.OBTAINED)
            },
        )
    }

    private fun goToCountryMissing() {
        requireActivity().navigateTo(
            destination = CountryListActivity::class.java,
            finishCurrent = true,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }

    private fun goToCardsRepeated() {
        requireActivity().navigateTo(
            destination = CardsRepeatedActivity::class.java,
            finishCurrent = true,
            extrasBuilder = {
                putExtra(EXTRA_CARD_TYPE, CardType.MISSING)
            },
        )
    }
}
