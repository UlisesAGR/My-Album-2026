/*
 * CardsViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.databinding.ItemCardsBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem

class CardsViewHolder(
    private val binding: ItemCardsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun render(
        item: CardsMissingItem.Cards,
        onCardsItemClick: () -> Unit
    ) = with(binding) {

    }
}
