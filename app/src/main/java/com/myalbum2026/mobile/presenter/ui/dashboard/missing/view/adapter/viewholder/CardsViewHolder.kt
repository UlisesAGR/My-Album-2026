/*
 * CardsViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.databinding.ItemCardsBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.InnerCardsAdapter

class CardsViewHolder(
    private val binding: ItemCardsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun render(
        item: CardsMissingItem.Cards,
        onCardsItemClick: (CardEntity) -> Unit,
    ) = with(binding) {
        setImageFlag(team = item.team)
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

    @SuppressLint("DiscouragedApi")
    private fun setImageFlag(team: TeamEntity) = with(binding) {
        val resId = context.resources.getIdentifier(
            team.flagResource,
            "drawable",
            context.packageName,
        )
        logoImageView.setImageResource(if (resId != 0) resId else R.drawable.il_flag_brazil)
    }
}
