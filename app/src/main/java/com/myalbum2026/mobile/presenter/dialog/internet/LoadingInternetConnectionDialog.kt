/*
 * LoadingInternetConnectionDialog
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.dialog.internet

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.DialogLoadingBinding

class LoadingInternetConnectionDialog: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogAnimation)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogLoadingBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .create().apply {
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            }.also {
                dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
            }
        dialog.setOnKeyListener { _, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val displayMetrics = resources.displayMetrics
            val maxWidth = (displayMetrics.widthPixels * 0.5).toInt()
            window.setLayout(
                maxWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            window.setGravity(Gravity.CENTER)
        }
    }

    companion object{
        const val TAG = "LoadingDialog"

        fun show(fragmentManager: FragmentManager){
            if(fragmentManager.findFragmentByTag(TAG) == null){
                LoadingInternetConnectionDialog().show(fragmentManager,TAG)
            }
        }
        fun dismiss(fragmentManager: FragmentManager){
            if(fragmentManager.findFragmentByTag(TAG) != null){
                val dialog = fragmentManager.findFragmentByTag(TAG) as DialogFragment
                dialog.dismissAllowingStateLoss()
            }
        }
    }
}