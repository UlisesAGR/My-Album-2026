/*
 * UserLocalDataSourceImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.datasource.user

import com.myalbum2026.mobile.data.datastore.PreferencesManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : UserLocalDataStore {

    override fun isFirstTime(): Flow<Boolean> =
        preferencesManager.isFirstTime

    override suspend fun setFirstTime() =
        preferencesManager.setFirstTime(isFirstTime = true)

    override fun isInfoShowed(): Flow<Boolean> =
        preferencesManager.isInfoShowed

    override suspend fun setInfoShowed() =
        preferencesManager.setInfoShowed(isInfoShowed = true)
}
