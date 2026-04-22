/*
 * InAppUpdateManager
 * Copyright © 2025. All rights reserved
 */
package com.myalbum2026.mobile.utils.extensions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateManager(
    activity: ComponentActivity,
    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {

    private val appUpdateManager = AppUpdateManagerFactory.create(activity)

    fun checkForImmediateUpdate(onComplete: () -> Unit) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isAllowed = info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)

            if (isAvailable && isAllowed) {
                val options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                appUpdateManager.startUpdateFlowForResult(info, updateLauncher, options)
            } else {
                onComplete()
            }
        }.addOnFailureListener {
            onComplete()
        }
    }

    fun resumeIfNeeded() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                val options = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                appUpdateManager.startUpdateFlowForResult(info, updateLauncher, options)
            }
        }
    }
}
