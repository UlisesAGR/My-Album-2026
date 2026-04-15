/*
 * CardsViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardsBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.InnerCardsAdapter

class CardsViewHolder(
    private val binding: ItemCardsBinding,
    private val viewPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {

    fun render(
        item: CardsMissingItem.Cards,
        onCardsItemClick: (CardEntity) -> Unit,
    ) = with(binding) {
        tvTeamName.text = item.team.countryName
        tvTeamCount.text = item.dates.size.toString()
        cardsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(root.context, 3)
            adapter = InnerCardsAdapter(
                cards = item.dates,
                onCardClick = { card ->
                    onCardsItemClick(card)
                },
            )
            setRecycledViewPool(viewPool)
        }
    }
}
