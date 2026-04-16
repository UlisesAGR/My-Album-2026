/*
 * ProgressViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.databinding.ItemProgressBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem

class ProgressViewHolder(
    private val binding: ItemProgressBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun render(item: CardsMissingItem.Progress) = with(binding) {
        with(item) {
            tvPercentage.text = percentage
            val progressInt = percentage.replace("%", "").toIntOrNull() ?: 0
            progressIndicator.setProgress(progressInt, true)
            tvTotalCards.text = total
            tvMissingCards.text = missing
        }
    }
}
