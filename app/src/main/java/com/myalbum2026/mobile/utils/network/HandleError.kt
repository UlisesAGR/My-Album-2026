/*
 * HandleError.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.network

import android.content.Context
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.utils.logger.log
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Context.handleError(exception: Throwable?): String {
    if (exception == null) return ""
    log(message = exception.toString())
    return when (exception) {
        is UnknownHostException, is ConnectException, is SocketException -> getString(R.string.check_your_internet_connection)
        is SocketTimeoutException, is TimeoutException -> getString(R.string.timeout)
        is HttpException -> getString(R.string.there_are_problems_with_the_servant_try_again_later)
        else -> getString(R.string.please_try_again_later)
    }
}
