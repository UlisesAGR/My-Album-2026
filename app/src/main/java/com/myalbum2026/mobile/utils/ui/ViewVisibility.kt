/*
 * ViewVisibility.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.view.View

fun View.show(): View {
    visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    visibility = View.GONE
    return this
}

fun View.invisible(): View {
    visibility = View.INVISIBLE
    return this
}

fun View.setVisibility(state: Boolean): View {
    visibility = if (state) View.VISIBLE else View.INVISIBLE
    return this
}
