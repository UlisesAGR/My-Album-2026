/*
 * BaseFragment
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.myalbum2026.mobile.presenter.dialog.internet.InternetConnectionDialog
import com.myalbum2026.mobile.utils.extensions.Constants.DIALOG_CONNECTION
import com.myalbum2026.mobile.utils.network.isInternetAvailable
import com.myalbum2026.mobile.presenter.widget.toolbar.CustomToolbar

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun init()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateInternetConnection(
            haveInternet = {
                init()
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupAppBar(
        toolbar: CustomToolbar,
        title: String,
        titleTextSize: Float = 18f,
        titleAlignment: Int = Gravity.CENTER_HORIZONTAL,
        iconLeft: Int? = null,
        actionLeftIcon: () -> Unit = {},
    ) {
        toolbar.apply {
            setTitle(title = title)
            setTitleTextSize(textSize = titleTextSize)
            iconLeft?.let  { iconLeft -> setIconLeft(icon = iconLeft) }
            setTitleAlignment(alignment = titleAlignment)
            setOnIconLeftClickListener {
                actionLeftIcon()
            }
        }
    }

    fun validateInternetConnection(haveInternet: () -> Unit = {}) {
        if (isInternetAvailable(context = requireContext())) {
            haveInternet()
        } else {
            showErrorInternetView(haveInternet)
        }
    }

    private fun showErrorInternetView(haveInternet: () -> Unit) {
        val dialogConnection = InternetConnectionDialog.newInstance(
            onDialogDismiss = {
                haveInternet()
            },
        )
        dialogConnection.show(parentFragmentManager, DIALOG_CONNECTION)
    }
}
