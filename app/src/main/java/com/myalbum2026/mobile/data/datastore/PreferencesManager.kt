/*
 * PreferencesManager.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

    val isFirstTime: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME] ?: true
        }

    suspend fun saveFirstTimeStatus(isFirstTime: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME] = isFirstTime
        }
    }
}
