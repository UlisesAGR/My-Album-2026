/*
 * RepositoryModule.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.di.repository

import com.myalbum2026.mobile.data.repository.album.AlbumRepositoryImpl
import com.myalbum2026.mobile.data.repository.user.UserRepositoryImpl
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import com.myalbum2026.mobile.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(albumRepositoryImpl: AlbumRepositoryImpl): AlbumRepository
}
