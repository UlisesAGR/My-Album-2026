/*
 * GetTeams.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import com.myalbum2026.mobile.domain.model.CardsItem

fun getMissingCardsFormattedText(cards: MutableList<CardsItem>?): String {
    val items = cards ?: return ""
    if (items.none { it is CardsItem.Card }) return ""
    val sb = StringBuilder()
    items.forEach { item ->
        when (item) {
            is CardsItem.TeamHeader -> {
                if (sb.isNotEmpty() && sb.endsWith(", ")) {
                    sb.setLength(sb.length - 2)
                }
                val flag = getFlagEmoji(item.team.id)
                sb.append("\n$flag ${item.team.id}: ")
            }
            is CardsItem.Card -> {
                sb.append("${item.card.number}, ")
            }
            else -> {}
        }
    }
    return sb.toString().trim().removeSuffix(",")
}

fun getRepeatedCardsFormattedText(cards: MutableList<CardsItem>?): String {
    val items = cards ?: return ""
    if (items.none { it is CardsItem.Card }) return ""
    val sb = StringBuilder()
    items.forEachIndexed { _, item ->
        when (item) {
            is CardsItem.TeamHeader -> {
                if (sb.isNotEmpty() && sb.endsWith(", ")) {
                    sb.setLength(sb.length - 2)
                }
                val flag = getFlagEmoji(item.team.id)
                sb.append("\n$flag ${item.team.id}: ")
            }
            is CardsItem.Card -> {
                val count = item.card.quantity - 1
                val text = if (count > 1) "${item.card.number} (x$count), "
                else "${item.card.number}, "
                sb.append(text)
            }
            else -> {}
        }
    }
    return sb.toString().trim().removeSuffix(",")
}
