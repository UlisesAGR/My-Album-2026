/*
 * AlbumDao.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity
import com.myalbum2026.mobile.data.model.TeamWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Transaction
    suspend fun insertFullAlbum(
        teams: List<TeamEntity>,
        cards: List<CardEntity>,
    ) {
        insertTeams(teams)
        insertCards(cards)
    }

    @Query("UPDATE cards SET quantity = :quantity, obtained = :hasIt WHERE id = :cardId")
    suspend fun updateCardStatus(
        cardId: String,
        quantity: Int,
        hasIt: Boolean,
    )

    @Transaction
    @Query("""
    SELECT * FROM teams 
    ORDER BY 
        CASE 
            WHEN id = 'SPECIALS' THEN 1 
            WHEN id = 'TIMELINE' THEN 3
            WHEN id = 'COCA-COLA' THEN 4
            ELSE 2 
        END, 
        countryName ASC
""")
    fun getFullAlbum(): Flow<List<TeamWithCards>>
}
