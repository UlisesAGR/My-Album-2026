/*
 * CardsViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.content.Context
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardsBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.InnerCardsAdapter
import com.myalbum2026.mobile.utils.ui.load

class CardsViewHolder(
    private val binding: ItemCardsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun render(
        item: CardsMissingItem.Cards,
        onCardsItemClick: (CardEntity) -> Unit,
    ) = with(binding) {
        logoImageView.load(
            uri = item.team.logoUrl.toUri(),
            loadImage = R.drawable.ic_blur_on,
            errorImage = R.drawable.ic_blur_on,
        )
        teamNameTextView.text = item.team.countryName
        teamCountTextView.text = context.getString(
            R.string.cards_value,
            item.dates.size,
        )

        cardsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CardsViewHolder.context, 3)
            adapter = InnerCardsAdapter(
                cards = item.dates,
                onCardClick = onCardsItemClick,
            )
        }
    }
}
