/*
 * CountryListAdapter.kt
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.countries.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.viewholder.TeamHeaderViewHolder

class CountryListAdapter(
    private val onTeamSelected: (TeamEntity) -> Unit = {},
) : ListAdapter<CardsItem.TeamHeader, com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.viewholder.TeamHeaderViewHolder>(CountryDiffCallback()), Filterable {

    private var fullList: List<CardsItem.TeamHeader> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.viewholder.TeamHeaderViewHolder {
        val binding = ItemTeamHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return _root_ide_package_.com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.viewholder.TeamHeaderViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: com.myalbum2026.mobile.presenter.ui.dashboard.home.missing.view.adapter.viewholder.TeamHeaderViewHolder, position: Int) {
        holder.render(
            item = getItem(position),
            onTeamSelected = onTeamSelected,
        )
    }

    fun updateData(newList: List<CardsItem.TeamHeader>) {
        fullList = newList
        submitList(newList)
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val query = constraint?.toString()?.lowercase().orEmpty()
            val filteredList = if (query.isEmpty()) { fullList
            } else {
                fullList.filter { card ->
                    card.team.countryName.lowercase().contains(query) ||
                    card.team.id.lowercase().contains(query)
                }
            }
            return FilterResults().apply { values = filteredList }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as? List<CardsItem.TeamHeader>)
        }
    }
}
