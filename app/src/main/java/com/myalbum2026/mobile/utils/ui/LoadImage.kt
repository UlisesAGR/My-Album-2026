/*
 * LoadImage.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.load(
    uri: Uri,
    loadImage: Int,
    errorImage: Int,
) {
    Glide.with(this)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(loadImage)
        .error(errorImage)
        .into(this)
}
