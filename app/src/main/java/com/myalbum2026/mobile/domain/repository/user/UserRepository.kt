package com.myalbum2026.mobile.domain.repository.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun isFirstTime(): Flow<Boolean>
    suspend fun saveFirstTimeStatus(isFirstTime: Boolean)
}