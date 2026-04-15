package com.myalbum2026.mobile.domain.usecase.user

import com.myalbum2026.mobile.domain.repository.user.UserRepository
import jakarta.inject.Inject

class SaveFirstTimeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(isFirstTime: Boolean) =
        repository.saveFirstTimeStatus(isFirstTime = isFirstTime)
}
