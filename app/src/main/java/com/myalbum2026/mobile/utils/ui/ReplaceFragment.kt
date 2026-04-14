/*
 * ReplaceFragment.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(
    containerViewId: FrameLayout,
    fragment: Fragment,
    addToBackStack: Boolean = false,
) {
    supportFragmentManager.beginTransaction().apply {
        replace(containerViewId.id, fragment)
        if (addToBackStack) {
            addToBackStack(null)
        }
        commitAllowingStateLoss()
    }
}

