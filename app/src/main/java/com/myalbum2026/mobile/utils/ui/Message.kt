/*
 * Message.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.toast(message: String) {
    message.takeIf { it.isNotEmpty() }?.let { text ->
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}

inline fun Context.materialDialog(
    style: Int,
    title: String,
    textNegativeButton: String,
    textPositiveButton: String,
    message: String,
    crossinline action: () -> Unit,
) {
    MaterialAlertDialogBuilder(this, style)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(textNegativeButton) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(textPositiveButton) { dialog, _ ->
            action()
            dialog.dismiss()
        }.show()
}
