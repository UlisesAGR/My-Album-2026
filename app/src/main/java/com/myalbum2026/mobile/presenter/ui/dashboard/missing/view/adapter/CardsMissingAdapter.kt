/*
 * CardsMissingAdapter.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardSingleBinding
import com.myalbum2026.mobile.databinding.ItemProgressBinding
import com.myalbum2026.mobile.databinding.ItemPublicityBinding
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.CardViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.ProgressViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.PublicityViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.TeamHeaderViewHolder

class CardsMissingAdapter(
    private var items: List<CardsMissingItem> = emptyList(),
    val onCardItemClick: (CardEntity) -> Unit = {},
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PUBLICITY = 0
        private const val TYPE_PROGRESS = 1
        private const val TYPE_TEAM_HEADER = 2
        private const val TYPE_CARD = 3
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is CardsMissingItem.Publicity -> TYPE_PUBLICITY
            is CardsMissingItem.Progress -> TYPE_PROGRESS
            is CardsMissingItem.TeamHeader -> TYPE_TEAM_HEADER
            is CardsMissingItem.Card -> TYPE_CARD
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            TYPE_PUBLICITY -> {
                PublicityViewHolder(
                    binding = ItemPublicityBinding.inflate(
                        inflater,
                        parent,
                        false,
                    )
                )
            }
            TYPE_PROGRESS -> {
                ProgressViewHolder(
                    binding = ItemProgressBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                )
            }
            TYPE_TEAM_HEADER -> {
                TeamHeaderViewHolder(
                    binding = ItemTeamHeaderBinding.inflate(
                        inflater,
                        parent,
                        false,
                    )
                )
            }
            else -> {
                CardViewHolder(
                    binding = ItemCardSingleBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                )
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = items[position]) {
            is CardsMissingItem.Publicity -> {
                (holder as PublicityViewHolder).render()
            }
            is CardsMissingItem.Progress -> {
                (holder as ProgressViewHolder).render(
                    item = item,
                )
            }
            is CardsMissingItem.TeamHeader -> {
                (holder as TeamHeaderViewHolder).render(item)
            }
            is CardsMissingItem.Card -> {
                (holder as CardViewHolder).render(
                    card = item.card,
                    onCardItemClick = onCardItemClick,
                )
            }
        }
    }

    override fun getItemCount(): Int =
        items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items: List<CardsMissingItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}
