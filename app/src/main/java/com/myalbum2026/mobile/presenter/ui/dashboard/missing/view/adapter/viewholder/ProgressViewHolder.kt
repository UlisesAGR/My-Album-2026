/*
 * ProgressViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ItemProgressBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsItem

class ProgressViewHolder(
    private val binding: ItemProgressBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    fun render(item: CardsItem.Progress) = with(binding) {
        with(item) {
            percentageTextView.text = percentage
            progressIndicator.setProgress(
                percentage.replace("%", "").toIntOrNull() ?: 0,
                true,
            )
            totalCardsTextView.text = total
            missingCardsTextView.text = missing
            setMissingCardsText(type = type)
        }
    }

    private fun setMissingCardsText(type: CardType) = with(binding) {
        progressTitleTextView.text = context.getString(
            if (type == CardType.MISSING) R.string.percentage_missing else R.string.percentage_obtained
        )
        textMissingCardsTextView.text = context.getString(
            if (type == CardType.MISSING) R.string.cards_obtained else R.string.missing_cards
        )
    }
}
