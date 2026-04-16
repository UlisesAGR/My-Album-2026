/*
 * PublicityViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.databinding.ItemPublicityBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem

class PublicityViewHolder(
    private val binding: ItemPublicityBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun render(
        item: CardsMissingItem.Publicity,
    ) = with(binding) {
        urlTextView.text = item.url
    }
}
