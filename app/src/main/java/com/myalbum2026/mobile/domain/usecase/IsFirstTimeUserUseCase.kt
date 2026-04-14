/*
 * IsFirstTimeUserUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase

import com.myalbum2026.mobile.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsFirstTimeUserUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        repository.isFirstTime()
}