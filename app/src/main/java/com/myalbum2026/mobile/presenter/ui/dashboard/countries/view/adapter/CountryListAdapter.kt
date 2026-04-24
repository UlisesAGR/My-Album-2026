/*
 * CountryListAdapter.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.countries.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.presenter.ui.dashboard.missing.view.adapter.viewholder.TeamHeaderViewHolder

@SuppressLint("NotifyDataSetChanged")
class CountryListAdapter(
    private var items: MutableList<CardsItem.TeamHeader> = mutableListOf(),
    private val onTeamSelected: (TeamEntity) -> Unit = {},
) : RecyclerView.Adapter<TeamHeaderViewHolder>(), Filterable {

    private var allItems: List<CardsItem.TeamHeader> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = TeamHeaderViewHolder(
        binding = ItemTeamHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
    )

    override fun onBindViewHolder(
        holder: TeamHeaderViewHolder,
        position: Int,
    ) {
        holder.render(
            item = items[position],
            onTeamSelected = onTeamSelected,
        )
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<CardsItem.TeamHeader>?) {
        newList?.let {
            this.allItems = it
            this.items = it.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter =
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase().orEmpty()
                val filteredList = if (query.isEmpty()) {
                    allItems
                } else {
                    allItems.filter { header ->
                        header.team.countryName.lowercase().contains(query) ||
                        header.team.id.lowercase().contains(query)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?,
            ) {
                items = (results?.values as? MutableList<CardsItem.TeamHeader>) ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
}
