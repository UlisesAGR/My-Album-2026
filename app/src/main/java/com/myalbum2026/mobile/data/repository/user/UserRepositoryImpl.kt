/*
 * UserRepositoryImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.repository.user

import com.myalbum2026.mobile.data.datasource.user.UserLocalDataStore
import com.myalbum2026.mobile.domain.repository.user.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserLocalDataStore,
) : UserRepository {

    override fun isFirstTime(): Flow<Boolean> =
        userDataStore.isFirstTime()

    override suspend fun setFirstTime() =
        userDataStore.setFirstTime()

    override fun isInfoShowed(): Flow<Boolean> =
        userDataStore.isInfoShowed()

    override suspend fun setInfoShowed() =
        userDataStore.setInfoShowed()
}
