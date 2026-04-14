/*
 * Intent.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.extensions

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.myalbum2026.mobile.R

@Suppress("DEPRECATION")
fun FragmentActivity.navigateTo(
    destination: Class<out FragmentActivity>,
    finishCurrent: Boolean = false,
    clearStack: Boolean = false,
    extrasBuilder: (Intent.() -> Unit)? = null,
) {
    val intent = Intent(this, destination)
    if (clearStack) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    extrasBuilder?.let { intent.apply(it) }
    startActivity(intent)
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    if (finishCurrent) {
        finish()
    }
}

inline fun OnBackPressedDispatcher.onBackPressedHandler(
    owner: LifecycleOwner,
    crossinline onClick: () -> Unit,
) {
    this.addCallback(
        owner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onClick()
            }
        },
    )
}

fun Dialog?.onBackPressed(onClick: () -> Unit) {
    this?.setOnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            onClick()
            return@setOnKeyListener true
        }
        false
    }
}

fun FragmentActivity.makeCall(
    phoneNumber: String,
    onError: (String) -> Unit = {},
) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    launchIntent(intent, onError)
}

fun FragmentActivity.sendWhatsApp(
    phoneNumber: String,
    message: String = "",
    onError: (String) -> Unit = {},
) {
    val uri = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    this.intent
    launchIntent(intent, onError)
}

fun FragmentActivity.sendEmail(
    email: String,
    subject: String = "",
    onError: (String) -> Unit = {},
) {
    val uriString = "mailto:${Uri.encode(email)}?subject=${Uri.encode(subject)}"
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = uriString.toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    launchIntent(intent, onError)
}

fun FragmentActivity.launchIntent(
    intent: Intent,
    onError: (String) -> Unit = {},
) {
    try {
        this.startActivity(intent)
    } catch (exception: Exception) {
        onError(exception.message.toString())
    }
}
