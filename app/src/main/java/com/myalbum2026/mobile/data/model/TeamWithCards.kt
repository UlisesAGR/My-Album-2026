package com.myalbum2026.mobile.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TeamWithCards(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "teamId",
    )
    val cards: List<CardEntity>,
)
