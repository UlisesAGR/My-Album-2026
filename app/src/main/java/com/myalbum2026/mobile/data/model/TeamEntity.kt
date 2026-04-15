package com.myalbum2026.mobile.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val countryName: String,
    val logoUrl: String,
    val totalCards: Int
)
