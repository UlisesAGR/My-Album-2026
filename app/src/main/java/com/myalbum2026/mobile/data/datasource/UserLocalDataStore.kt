/*
 * UserLocalDataStore.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.datasource

import kotlinx.coroutines.flow.Flow

interface UserLocalDataStore {
    fun isFirstTime(): Flow<Boolean>
}
