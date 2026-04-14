/*
 * NetworkResult
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.network

import com.myalbum2026.mobile.utils.extensions.Constants.REQUEST_NO_CONTENT
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

abstract class BaseApiResponse {

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> =
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null || response.code() != REQUEST_NO_CONTENT) {
                    NetworkResult.Success(data = body!!)
                } else {
                    failure(message = "${response.code()} -> ${response.message()}")
                }
            } else {
                failure(message = "${response.code()} -> ${response.message()}")
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            when (exception) {
                is TimeoutCancellationException, is SocketTimeoutException, is TimeoutException -> {
                    failure(
                        message = "TimeoutException: ${exception.message}",
                        errorType = NetworkErrorType.TIMEOUT,
                    )
                }
                is UnknownHostException, is ConnectException, is SocketException -> {
                    failure(
                        message = "RedException: ${exception.message}",
                        errorType = NetworkErrorType.NETWORK,
                    )
                }
                is HttpException -> {
                    failure(
                        message = "Error HTTP: ${exception.code()}",
                        errorType = NetworkErrorType.HTTP,
                    )
                }
                else -> {
                    failure(
                        message = "UnknowException: ${exception.message}",
                        errorType = NetworkErrorType.UNKNOWN,
                    )
                }
            }
        }
}

private fun <T> failure(
    message: String,
    errorType: NetworkErrorType = NetworkErrorType.UNKNOWN,
): NetworkResult<T> =
    NetworkResult.Error(errorMessage = message, errorType = errorType)

data class ResourceLoading<out T>(val status: StatusLoading) {
    companion object {
        fun <T> loading(): ResourceLoading<T> = ResourceLoading(StatusLoading.SHOW_LOADING)
        fun <T> dismissLoading(): ResourceLoading<T> =
            ResourceLoading(StatusLoading.DISMISS_LOADING)
    }
}

data class Resource<out T>(
    val status: Status,
    val data: T?, val message: String?,
) {
    companion object {
        fun <T> success(data: T?): Resource<T> =
            Resource(Status.SUCCESS, data, null)
        fun <T> error(msg: String, data: T?): Resource<T> =
            Resource(Status.ERROR, data, msg)
    }
}

sealed class NetworkResult<T>(
    val data: T? = null,
    val errorMessage: String? = null,
    val errorType: NetworkErrorType? = null,
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(
        errorMessage: String,
        errorType: NetworkErrorType = NetworkErrorType.UNKNOWN,
        data: T? = null,
    ) : NetworkResult<T>(data, errorMessage, errorType)
}

sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String = "") : UiState<Nothing>()
}

sealed class UIStateAlert<out T> {
    data class Success<out T>(val data: T) : UIStateAlert<T>()
    data class Error(
        val message: String = "",
        val title: String = "",
        val alertType: String = "error",
    ) : UIStateAlert<Nothing>()
}

enum class NetworkErrorType {
    TIMEOUT,
    NETWORK,
    HTTP,
    UNKNOWN,
}
