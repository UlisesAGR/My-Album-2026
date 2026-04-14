/*
 * FlowCollect.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> LifecycleOwner.collect(
    flow: Flow<T>,
    crossinline action: suspend (T) -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { data ->
                action(data)
            }
        }
    }
}

inline fun <T> Fragment.collect(
    flow: Flow<T>,
    crossinline action: suspend (T) -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { data ->
                action(data)
            }
        }
    }
}
