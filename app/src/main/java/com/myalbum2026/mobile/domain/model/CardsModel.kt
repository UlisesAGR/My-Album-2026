/*
 * CardsModel.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.domain.model

data class CardsModel(
    val id: String,
    val number: Int,
    val type: String,
    val position: String,
    val obtained: Boolean,
    val quantity: Int = 0,
)
