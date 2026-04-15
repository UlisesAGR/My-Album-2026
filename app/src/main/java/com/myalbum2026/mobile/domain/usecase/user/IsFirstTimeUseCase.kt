package com.myalbum2026.mobile.domain.usecase.user

import com.myalbum2026.mobile.domain.repository.user.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsFirstTimeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        repository.isFirstTime()
}