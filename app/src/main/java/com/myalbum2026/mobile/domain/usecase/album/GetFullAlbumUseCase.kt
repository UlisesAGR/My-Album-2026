/*
 * GetFullAlbumUseCase.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.usecase.album

import com.myalbum2026.mobile.data.model.TeamWithCards
import com.myalbum2026.mobile.domain.repository.album.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFullAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository,
) {
    operator fun invoke(): Flow<List<TeamWithCards>> =
        repository.getFullAlbum()
}
