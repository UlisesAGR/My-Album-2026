/*
 * BaseOnlyActivity
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.base

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import com.myalbum2026.mobile.utils.extensions.Constants.DIALOG_CONNECTION
import com.myalbum2026.mobile.utils.network.isInternetAvailable
import com.myalbum2026.mobile.presenter.dialog.internet.InternetConnectionDialog
import com.myalbum2026.mobile.presenter.widget.toolbar.CustomToolbar

abstract class BaseOnlyActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun inflateBinding(): VB
    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding()
        setContentView(binding.root)
        applyEdgeToEdgeIfNeeded()
        init()
    }

    fun setupAppBar(
        toolbar: CustomToolbar,
        title: String,
        titleTextSize: Float = 18f,
        iconLeft: Int? = null,
        titleAlignment: Int = Gravity.CENTER_HORIZONTAL,
        actionLeftIcon: () -> Unit = {},
    ) {
        toolbar.apply {
            setTitle(title = title)
            setTitleTextSize(textSize = titleTextSize)
            iconLeft?.let { icon -> setIconLeft(icon = icon) }
            setTitleAlignment(alignment = titleAlignment)
            setOnIconLeftClickListener {
                actionLeftIcon()
            }
        }
    }

    fun validateInternetConnection(haveInternet: () -> Unit = {}) {
        if (isInternetAvailable(context = this)) {
            haveInternet()
        } else {
            showErrorInternetView(haveInternet)
        }
    }

    private fun showErrorInternetView(haveInternet: () -> Unit) {
        if (!supportFragmentManager.isStateSaved) {
            val dialogConnection = InternetConnectionDialog.newInstance(
                onDialogDismiss = {
                    haveInternet()
                },
            )
            dialogConnection.show(supportFragmentManager, DIALOG_CONNECTION)
        }
    }

    private fun applyEdgeToEdgeIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
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
