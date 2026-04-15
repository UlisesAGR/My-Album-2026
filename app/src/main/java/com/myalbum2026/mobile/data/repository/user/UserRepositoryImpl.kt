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

    override suspend fun saveFirstTimeStatus(isFirstTime: Boolean) =
        userDataStore.saveFirstTimeStatus(isFirstTime = isFirstTime)
}