/*
 * AlbumDataResponse.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

import com.google.gson.annotations.SerializedName

data class AlbumDataResponse(
    @SerializedName("sections") val sections: List<TeamRemoteEntity>,
)
