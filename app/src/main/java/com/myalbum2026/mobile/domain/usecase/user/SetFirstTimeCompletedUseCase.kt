/*
 * SetFirstTimeCompletedUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase.user

import com.myalbum2026.mobile.domain.repository.user.UserRepository
import jakarta.inject.Inject

class SetFirstTimeCompletedUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke() =
        repository.saveFirstTimeStatus()
}
