/*
 * SetOnSafeClickListener.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.view.View
import androidx.annotation.VisibleForTesting
import com.myalbum2026.mobile.utils.ui.SafeClick.Companion.CLICK_THRESHOLD_MS
import java.lang.ref.WeakReference

fun View.setOnSafeClickListener(
    clickThreshold: Long,
    listener: View.OnClickListener,
) {
    val weakView = WeakReference(this)
    setOnClickListener(
        object : OnSafeClickListener(clickThreshold) {
            override fun onSafeClick(view: View) {
                weakView.get()?.let { listener.onClick(it) }
            }
        },
    )
}

fun View.setOnSafeClickListener(
    clickThreshold: Long = CLICK_THRESHOLD_MS,
    onClick: (v: View) -> Unit,
) {
    val weakView = WeakReference(this)
    setOnClickListener(
        object : OnSafeClickListener(clickThreshold) {
            override fun onSafeClick(view: View) {
                weakView.get()?.let { onClick(it) }
            }
        },
    )
}

fun View.setOnSafeClickListener(listener: View.OnClickListener) {
    setOnSafeClickListener(CLICK_THRESHOLD_MS, listener)
}

class SafeClick {
    companion object {
        @VisibleForTesting
        const val CLICK_THRESHOLD_MS = 700L

        @VisibleForTesting
        var lastEmittedClickTimestamp = 0L

        private val lock = Any()

        @JvmStatic
        @JvmOverloads
        fun execute(
            clickThreshold: Long = CLICK_THRESHOLD_MS,
            lambda: () -> Unit,
        ) {
            synchronized(lock) {
                System.currentTimeMillis().also {
                    if (it > lastEmittedClickTimestamp + clickThreshold || it < lastEmittedClickTimestamp) {
                        lastEmittedClickTimestamp = it
                        lambda()
                    }
                }
            }
        }
    }
}

@VisibleForTesting
abstract class OnSafeClickListener(private val clickThreshold: Long) : View.OnClickListener {
    abstract fun onSafeClick(view: View)

    override fun onClick(view: View) {
        SafeClick.execute(clickThreshold) {
            onSafeClick(view)
        }
    }
}
