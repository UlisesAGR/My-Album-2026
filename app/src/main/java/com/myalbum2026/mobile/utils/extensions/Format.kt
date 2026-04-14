/*
 * Format.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.extensions

import android.annotation.SuppressLint
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("ConstantLocale")
private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun Int.intToString(): String = toString()

fun EditText.getInt(): Int = text.toString().toIntOrNull() ?: 0

fun EditText.getString(): String = text.toString()

fun String.equalsIgnoreCase(other: String): Boolean =
    this.equals(other, ignoreCase = true)
