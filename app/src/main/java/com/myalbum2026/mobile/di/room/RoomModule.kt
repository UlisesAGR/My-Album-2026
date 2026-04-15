/*
 * RoomModule.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.di.room

import android.content.Context
import androidx.room.Room
import com.myalbum2026.mobile.BuildConfig.DATABASE_NAME
import com.myalbum2026.mobile.data.database.AlbumDao
import com.myalbum2026.mobile.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideAlbumDao(database: Database): AlbumDao =
        database.albumDao()
}
