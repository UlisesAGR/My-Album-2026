/*
 * Version.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.extensions

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.getVersionName(): String =
    try {
        this.packageManager?.getPackageInfo(
            this.packageName.orEmpty(),
            0,
        )?.versionName.orEmpty()
    } catch (exception: PackageManager.NameNotFoundException) {
        exception.printStackTrace()
        ""
    }
