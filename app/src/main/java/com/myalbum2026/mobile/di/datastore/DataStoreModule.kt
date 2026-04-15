/*
 * DataStoreModule.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.di.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.myalbum2026.mobile.data.datastore.PreferencesManager
import com.myalbum2026.mobile.data.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun providePreferencesManager(dataStore: DataStore<Preferences>): PreferencesManager =
        PreferencesManager(dataStore)
}
