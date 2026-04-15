/*
 * AppModule.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.di.app

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager =
        context.assets

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
