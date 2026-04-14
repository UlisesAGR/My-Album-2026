/*
 * UserDataSourceModule.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.di.datasource

import com.myalbum2026.mobile.data.datasource.UserLocalDataSourceImpl
import com.myalbum2026.mobile.data.datasource.UserLocalDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindUserDataSource(userLocalDataSourceImpl: UserLocalDataSourceImpl): UserLocalDataStore
}
