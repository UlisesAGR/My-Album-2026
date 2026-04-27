/*
 * CardsDiffCallback.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.myalbum2026.mobile.domain.model.CardsItem

class CardsDiffCallback : DiffUtil.ItemCallback<CardsItem>() {

    override fun areItemsTheSame(
        oldItem: CardsItem,
        newItem: CardsItem,
    ): Boolean =
        when (oldItem) {
            is CardsItem.Card if newItem is CardsItem.Card ->
                oldItem.card.id == newItem.card.id

            is CardsItem.TeamHeader if newItem is CardsItem.TeamHeader ->
                oldItem.team.id == newItem.team.id

            else -> oldItem.javaClass == newItem.javaClass
        }

    override fun areContentsTheSame(
        oldItem: CardsItem,
        newItem: CardsItem,
    ): Boolean = oldItem == newItem
}
