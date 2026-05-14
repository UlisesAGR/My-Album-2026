/*
 * ViewVisibility.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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
    visibility = if (state) View.VISIBLE else View.GONE
    return this
}

fun Activity.setScreenBrightness(brightness: Float) {
    val layoutParams = this.window.attributes
    layoutParams.screenBrightness = brightness
    this.window.attributes = layoutParams
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
