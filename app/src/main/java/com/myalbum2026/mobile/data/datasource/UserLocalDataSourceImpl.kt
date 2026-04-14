/*
 * UserLocalDataSourceImpl.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.datasource

import com.myalbum2026.mobile.data.datastore.UserPreferencesManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl @Inject constructor(
    private val preferencesManager: UserPreferencesManager,
) : UserLocalDataStore {

    override fun isFirstTime(): Flow<Boolean> =
        preferencesManager.isFirstTime
}
