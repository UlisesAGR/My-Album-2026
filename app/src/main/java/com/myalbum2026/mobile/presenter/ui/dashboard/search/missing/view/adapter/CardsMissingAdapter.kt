/*
 * CardsMissingAdapter.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardSingleBinding
import com.myalbum2026.mobile.databinding.ItemProgressBinding
import com.myalbum2026.mobile.databinding.ItemPublicityBinding
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter.viewholder.CardViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter.viewholder.ProgressViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter.viewholder.PublicityViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter.viewholder.TeamHeaderViewHolder
import com.myalbum2026.mobile.utils.binding.inflateBinding

class CardsMissingAdapter(
    val cardType: CardType,
    val onCardItemClick: (CardEntity) -> Unit = {},
) : ListAdapter<CardsItem, RecyclerView.ViewHolder>(CardsDiffCallback()) {

    companion object {
        private const val TYPE_PUBLICITY = 0
        private const val TYPE_PROGRESS = 1
        private const val TYPE_TEAM_HEADER = 2
        private const val TYPE_CARD = 3
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is CardsItem.Publicity -> TYPE_PUBLICITY
            is CardsItem.Progress -> TYPE_PROGRESS
            is CardsItem.TeamHeader -> TYPE_TEAM_HEADER
            is CardsItem.Card -> TYPE_CARD
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_PUBLICITY -> PublicityViewHolder(binding = parent.inflateBinding(bindingInflater = ItemPublicityBinding::inflate))
            TYPE_PROGRESS -> ProgressViewHolder(binding = parent.inflateBinding(bindingInflater = ItemProgressBinding::inflate))
            TYPE_TEAM_HEADER -> TeamHeaderViewHolder(binding = parent.inflateBinding(bindingInflater = ItemTeamHeaderBinding::inflate))
            else -> CardViewHolder(binding = parent.inflateBinding(bindingInflater = ItemCardSingleBinding::inflate))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is PublicityViewHolder -> holder.render()
            is ProgressViewHolder -> holder.render(item = item as CardsItem.Progress)
            is TeamHeaderViewHolder -> holder.render(item = item as CardsItem.TeamHeader)
            is CardViewHolder -> holder.render(
                cardType = cardType,
                card = (item as CardsItem.Card).card,
                onCardItemClick = onCardItemClick,
            )
        }
    }
}
