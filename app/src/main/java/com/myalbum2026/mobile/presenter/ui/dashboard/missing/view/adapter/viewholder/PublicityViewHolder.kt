/*
 * PublicityViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.myalbum2026.mobile.databinding.ItemPublicityBinding

class PublicityViewHolder(
    private val binding: ItemPublicityBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun render() = with(binding) {
        bannerPublicity.loadAd(AdRequest.Builder().build())
    }
}
