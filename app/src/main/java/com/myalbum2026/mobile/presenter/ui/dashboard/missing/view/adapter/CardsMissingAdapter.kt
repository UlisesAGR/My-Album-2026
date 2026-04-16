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
import com.myalbum2026.mobile.databinding.ItemCardsBinding
import com.myalbum2026.mobile.databinding.ItemProgressBinding
import com.myalbum2026.mobile.databinding.ItemPublicityBinding
import com.myalbum2026.mobile.domain.model.CardsMissingItem
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.CardsViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.ProgressViewHolder
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.PublicityViewHolder

class CardsMissingAdapter(
    private var items: List<CardsMissingItem> = emptyList(),
    val onCardsItemClick: (CardEntity) -> Unit = {},
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PUBLICITY = 0
        private const val TYPE_PROGRESS = 1
        private const val TYPE_CARDS = 2
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is CardsMissingItem.Publicity -> TYPE_PUBLICITY
            is CardsMissingItem.Progress -> TYPE_PROGRESS
            is CardsMissingItem.Cards -> TYPE_CARDS
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when(viewType) {
            TYPE_PUBLICITY -> PublicityViewHolder(
                binding = ItemPublicityBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
            TYPE_PROGRESS -> {
                ProgressViewHolder(
                    binding = ItemProgressBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }
            else -> {
                CardsViewHolder(
                    binding = ItemCardsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = items[position]) {
            is CardsMissingItem.Publicity -> {
                (holder as PublicityViewHolder).render(
                    item = item,
                )
            }
            is CardsMissingItem.Progress -> {
                (holder as ProgressViewHolder).render(
                    item = item,
                )
            }
            is CardsMissingItem.Cards -> {
                (holder as CardsViewHolder).render(
                    item = item,
                    onCardsItemClick = onCardsItemClick,
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
