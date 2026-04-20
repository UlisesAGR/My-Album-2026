/*
 * TeamHeaderViewHolder.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardType
import com.myalbum2026.mobile.domain.model.CardsMissingItem

class TeamHeaderViewHolder(
    private val binding: ItemTeamHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun render(item: CardsMissingItem.TeamHeader) = with(binding) {
        with(item) {
            setImageFlag(team = team)
            teamNameTextView.text = team.countryName
            setHeaderText(item = this)
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun setImageFlag(team: TeamEntity) = with(binding) {
        val resId = context.resources.getIdentifier(
            team.flagResource,
            "drawable",
            context.packageName,
        )
        logoImageView.setImageResource(if (resId != 0) resId else R.drawable.il_flag_europe)
    }

    private fun setHeaderText(item: CardsMissingItem.TeamHeader) = with(binding) {
        with(item) {
            teamCountTextView.text = context.getString(
                R.string.header_format,
                count,
                total,
                context.getString(if (type == CardType.OBTAINED) R.string.obtained else R.string.missing),
            )
        }
    }
}
