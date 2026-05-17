/*
 * TeamListAdapter.kt
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.search.countries.view.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.databinding.ItemTeamHeaderBinding
import com.myalbum2026.mobile.domain.model.CardsItem
import com.myalbum2026.mobile.presenter.ui.dashboard.search.missing.view.adapter.viewholder.TeamHeaderViewHolder
import com.myalbum2026.mobile.utils.binding.inflateBinding

class TeamListAdapter(
    private val onTeamSelected: (TeamEntity) -> Unit = {},
) : ListAdapter<CardsItem.TeamHeader, TeamHeaderViewHolder>(TeamDiffCallback()), Filterable {

    private var fullList: List<CardsItem.TeamHeader> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamHeaderViewHolder = TeamHeaderViewHolder(
        binding = parent.inflateBinding(bindingInflater = ItemTeamHeaderBinding::inflate),
    )

    override fun onBindViewHolder(
        holder: TeamHeaderViewHolder,
        position: Int,
    ) {
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
