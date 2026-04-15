/*
 * Database.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myalbum2026.mobile.data.model.CardEntity
import com.myalbum2026.mobile.data.model.TeamEntity

@Database(
    entities = [
        TeamEntity::class,
        CardEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
