/*
 * CountryDiffCallback.kt
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.myalbum2026.mobile.domain.model.CardsItem

class CountryDiffCallback : DiffUtil.ItemCallback<CardsItem.TeamHeader>() {

    override fun areItemsTheSame(
        oldItem: CardsItem.TeamHeader,
        newItem: CardsItem.TeamHeader,
    ): Boolean = oldItem.team.id == newItem.team.id

    override fun areContentsTheSame(
        oldItem: CardsItem.TeamHeader,
        newItem: CardsItem.TeamHeader,
    ): Boolean = oldItem == newItem
}
