/*
 * UpdateCardUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase.album

import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCardUseCase @Inject constructor(
    private val repository: AlbumRepository,
) {
    suspend operator fun invoke(
        cardId: String,
        quantity: Int,
        hasIt: Boolean,
    ): Flow<Unit> = repository.updateCardStatus(
        cardId = cardId,
        quantity = quantity,
        hasIt = hasIt,
    )
}
