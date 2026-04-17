/*
 * CardViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.content.Context
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.databinding.ItemCardSingleBinding
import com.myalbum2026.mobile.utils.ui.getTeamColor

class CardViewHolder(
    private val binding: ItemCardSingleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    fun render(
        card: CardEntity,
        onCardItemClick: (CardEntity) -> Unit,
    ) = with(binding) {
        with(card) {
            setBackgroundCounty(card = card)
            idNumber.text = id
            cardNumber.text = number.toString()
            cardPosition.text = position.orEmpty()
            cardQuantity.text = quantity.toString()
            root.setOnClickListener {
                onCardItemClick(this)
            }
        }
    }

    private fun setBackgroundCounty(card: CardEntity) {
        binding.itemRoot.backgroundTintList =
            ColorStateList.valueOf(context.getTeamColor(card.teamId))
    }
}
