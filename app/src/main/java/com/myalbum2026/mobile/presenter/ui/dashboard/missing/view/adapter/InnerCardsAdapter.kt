/*
 * InnerCardsAdapter.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardBinding
import com.myalbum2026.mobile.utils.ui.getTeamColor

class InnerCardsAdapter(
    private val cards: List<CardEntity>,
    private val onCardClick: (CardEntity) -> Unit,
) : RecyclerView.Adapter<InnerCardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
        ): ViewHolder = ViewHolder(
        binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
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

        private val context: Context = binding.root.context

        fun render(
            card: CardEntity,
            onCardClick: (CardEntity) -> Unit,
        ) = with(binding) {
            with(card) {
                setBackgroundCounty(card = card)
                idNumber.text = id
                cardNumber.text = number.toString()
                cardPosition.text = position.orEmpty()
                cardQuantity.text = quantity.toString()
                root.setOnClickListener {
                    onCardClick(this)
                }
            }
        }

        private fun setBackgroundCounty(card: CardEntity) {
            binding.itemRoot.backgroundTintList =
                ColorStateList.valueOf(context.getTeamColor(card.teamId))
        }
    }
}
