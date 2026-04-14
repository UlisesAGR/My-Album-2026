/*
 * InternetConnectionDialog
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.dialog.internet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.FragmentDialogConnectionInternetBinding
import com.myalbum2026.mobile.utils.binding.viewBinding
import com.myalbum2026.mobile.utils.extensions.Constants.DELAY
import com.myalbum2026.mobile.utils.network.isInternetAvailable
import com.myalbum2026.mobile.utils.ui.setAnimationStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InternetConnectionDialog : DialogFragment(R.layout.fragment_dialog_connection_internet) {

    private val binding by viewBinding(FragmentDialogConnectionInternetBinding::bind)

    var onDialogDismiss: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyEdgeToEdgeIfNeeded()
        requireDialog().setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        setInit()
    }

    private fun setInit() {
        setAnimationIn()
        setListeners()
    }

    private fun setAnimationIn() {
        binding.root.setAnimationStart(animationId = R.anim.slide_in_left)
    }

    private fun setListeners() = with(binding) {
        containerErrorInternet.retryCustomButton.setOnClickListener {
            LoadingInternetConnectionDialog.show(requireActivity().supportFragmentManager)
            viewLifecycleOwner.lifecycleScope.launch {
                delay(DELAY)
                LoadingInternetConnectionDialog.dismiss(requireActivity().supportFragmentManager)
                if (isInternetAvailable(requireContext())) {
                    if (isAdded && !parentFragmentManager.isStateSaved) {
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss?.invoke()
    }

    private fun applyEdgeToEdgeIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            dialog?.window?.let { window ->
                window.isNavigationBarContrastEnforced = false
                WindowCompat.setDecorFitsSystemWindows(window, false)
                ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    view.updatePadding(
                        top = systemBars.top,
                        bottom = systemBars.bottom,
                    )
                    insets
                }
            }
        }
    }

    companion object {
        fun newInstance(
            onDialogDismiss: (() -> Unit)? = null,
        ): InternetConnectionDialog =
            InternetConnectionDialog().apply {
                this.onDialogDismiss = onDialogDismiss
            }
    }
}
