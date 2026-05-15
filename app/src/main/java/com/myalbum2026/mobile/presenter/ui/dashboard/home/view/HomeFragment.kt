package com.myalbum2026.mobile.presenter.ui.dashboard.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.FragmentHomeBinding
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.dialog.loading.LoadingDialog
import com.myalbum2026.mobile.presenter.ui.dashboard.home.viewmodel.HomeUiEvent
import com.myalbum2026.mobile.presenter.ui.dashboard.home.viewmodel.HomeViewModel
import com.myalbum2026.mobile.utils.base.BaseFragment
import com.myalbum2026.mobile.utils.extensions.collect
import com.myalbum2026.mobile.utils.extensions.getVersionName
import com.myalbum2026.mobile.utils.logger.log
import com.myalbum2026.mobile.utils.network.handleError
import com.myalbum2026.mobile.utils.ui.startParty
import com.myalbum2026.mobile.utils.ui.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun init() {
        setText()
        setFlows()
    }

    private fun setText() = with(binding)  {
        versionTextView.text = getString(
            R.string.version_value,
            requireActivity().getVersionName(),
        )
    }

    private fun setFlows() {
        collect(homeViewModel.homeUiState) { state ->
            statusLoading(isLoading = state.isLoading)
            updateProgress(items = state.items)
        }
        collect(homeViewModel.homeUiEvent) { state ->
            with(state) {
                when (this) {
                    is HomeUiEvent.Idle -> log(message = getString(R.string.idle))
                    is HomeUiEvent.ShowError -> requireContext().toast(message = requireContext().handleError(exception))
                }
            }
        }
    }

    private fun statusLoading(isLoading: Boolean) {
        if (isLoading) LoadingDialog.show(childFragmentManager)
        else LoadingDialog.dismiss(childFragmentManager)
    }

    private fun updateProgress(items: MutableList<CardsItem>) = with(binding) {
        items.filterIsInstance<CardsItem.Progress>().firstOrNull()?.let { progress ->
            with(progress) {
                val percentageNoFormat = percentage.replace("%", "").toIntOrNull() ?: 0

                progressBar.progress = percentageNoFormat
                percentageTextView.text = percentage
                myProgressObtainedTextView.text = getString(
                    R.string.progress_obtained_format,
                    obtained,
                    total,
                )

                myProgressMissingTextView.text = missing
                myProgressRepeatedTextView.text = repeated

                validateIfAlbumIsCompleted(percentageNoFormat)
            }
        }
    }

    private fun validateIfAlbumIsCompleted(percentageNoFormat: Int) {
        if (percentageNoFormat == 100) {
            binding.completeConfettiView.startParty()
        }
    }
}
