/*
 * UserRepositoryImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.repository

import com.myalbum2026.mobile.data.datasource.UserLocalDataStore
import com.myalbum2026.mobile.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserLocalDataStore,
) : UserRepository {

    override fun isFirstTime(): Flow<Boolean> =
        userDataStore.isFirstTime()
}