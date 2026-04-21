/*
 * PublicityViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ItemPublicityBinding
import com.myalbum2026.mobile.utils.ui.gone
import com.myalbum2026.mobile.utils.ui.show

class PublicityViewHolder(
    private val binding: ItemPublicityBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun render() = with(binding) {
        val shouldShowAds = context.resources.getBoolean(R.bool.show_ads)
        if (shouldShowAds) {
            bannerPublicity.apply {
                loadAd(AdRequest.Builder().build())
                show()
            }
        } else {
            bannerPublicity.gone()
        }
    }
}
