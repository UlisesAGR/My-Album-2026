/*
 * CardsDiffCallback.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.myalbum2026.mobile.domain.model.CardsItem

class CardsDiffCallback(
    private val oldList: List<CardsItem>,
    private val newList: List<CardsItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when (oldItem) {
            is CardsItem.Card if newItem is CardsItem.Card ->
                oldItem.card.id == newItem.card.id

            is CardsItem.TeamHeader if newItem is CardsItem.TeamHeader ->
                oldItem.team.countryName == newItem.team.countryName

            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
