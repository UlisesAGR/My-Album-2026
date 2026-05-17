/*
 * CountryViewHolder.kt
 */
package com.myalbum2026.mobile.presenter.ui.dashboard.home.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.databinding.ItemCountryHeaderBinding
import com.myalbum2026.mobile.domain.model.CardsItem

class CountryViewHolder(
    private val binding: ItemCountryHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    val context: Context = binding.root.context

    fun render(
        item: CardsItem.TeamHeader,
        onTeamSelected: (TeamEntity) -> Unit = {},
    ) = with(binding) {
        with(item) {
            setImageFlag(team = team)
            countryNameTextView.text = team.countryName
            progressBarCountry.progress = progress
            progressTextView.text = context.getString(
                R.string.progress_obtained_format,
                obtained.toString(),
                total.toString(),
            )
            missingCountTextView.text = missing.toString()

            root.setOnClickListener {
                onTeamSelected(item.team)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun setImageFlag(team: TeamEntity) = with(binding) {
        val resId = context.resources.getIdentifier(
            team.flagResource,
            "drawable",
            context.packageName,
        )
        flagImageView.setImageResource(if (resId != 0) resId else R.drawable.il_flag_europe)
    }
}
