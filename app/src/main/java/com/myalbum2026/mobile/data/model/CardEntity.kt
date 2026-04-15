package com.myalbum2026.mobile.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["teamId"])],
)
data class CardEntity(
    @PrimaryKey val id: String,
    val teamId: String,
    val number: Int,
    val type: String,
    val position: String?,
    val obtained: Boolean = false,
    val quantity: Int = 0,
)
