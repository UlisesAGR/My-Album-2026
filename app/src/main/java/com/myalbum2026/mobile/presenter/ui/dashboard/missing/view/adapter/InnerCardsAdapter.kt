/*
 * InnerCardsAdapter.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardBinding

class InnerCardsAdapter(
    private val cards: List<CardEntity>,
    private val onCardClick: (CardEntity) -> Unit,
) : RecyclerView.Adapter<InnerCardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        ): ViewHolder =
        ViewHolder(
            binding = ItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(
            card = cards[position],
            onCardClick = onCardClick,
        )
    }

    override fun getItemCount(): Int =
        cards.size

    class ViewHolder(
        val binding: ItemCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun render(
            card: CardEntity,
            onCardClick: (CardEntity) -> Unit,
        ) = with(binding) {
            cardNumber.text = card.number.toString()
            cardPosition.text = card.position.orEmpty()
            cardQuantity.text = card.quantity.toString()
            root.setOnClickListener {
                onCardClick(card)
            }
        }
    }
}
