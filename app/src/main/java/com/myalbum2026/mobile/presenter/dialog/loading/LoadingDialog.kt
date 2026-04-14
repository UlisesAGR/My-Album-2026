/*
 * LoadingDialog
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.dialog.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.myalbum2026.mobile.R

class LoadingDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.dialog_loading, container, false)

    companion object {
        const val TAG = "LoadingDialog"
        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                LoadingDialog().show(fragmentManager, TAG)
            }
        }
        fun dismiss(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) != null) {
                val dialog = fragmentManager.findFragmentByTag(TAG) as DialogFragment
                dialog.dismissAllowingStateLoss()
            }
        }
    }
}
