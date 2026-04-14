/*
 * Animation.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import java.lang.ref.WeakReference



fun View.setAnimationStart(animationId: Int) {
    val animation = AnimationUtils.loadAnimation(context, animationId)
    val weakReference = WeakReference(this)
    animation.setAnimationListener(
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit

            override fun onAnimationEnd(animation: Animation?) {
                weakReference.get()?.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        },
    )
    this.startAnimation(animation)
}

inline fun View.setAnimationEnd(
    animationId: Int,
    crossinline onAnimationEnd: () -> Unit,
) {
    val animation = AnimationUtils.loadAnimation(context, animationId)
    val weakReference = WeakReference(this)
    animation.setAnimationListener(
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) = Unit

            override fun onAnimationEnd(animation: Animation?) {
                onAnimationEnd()
                weakReference.get()?.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) = Unit
        },
    )
    this.startAnimation(animation)
}
