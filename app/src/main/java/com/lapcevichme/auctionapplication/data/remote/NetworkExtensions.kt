package com.lapcevichme.auctionapplication.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Result<T> {
    return try {
        Result.success(apiCall())
    } catch (e: Exception) {
        if (e is CancellationException) {
            throw e
        }

        val mappedException = mapNetworkException(e)
        Result.failure(mappedException)
    }
}

private fun mapNetworkException(e: Exception): Exception {
    return when (e) {
        is ClientRequestException -> {
            Exception("Ошибка клиента: ${e.response.status.value}", e)
        }
        // 5xx
        is ServerResponseException -> {
            Exception("Сервер временно недоступен (${e.response.status.value})", e)
        }

        is IOException -> {
            Exception("Проверьте соединение с интернетом", e)
        }

        else -> {
            Exception("Неизвестная ошибка: ${e.localizedMessage}", e)
        }
    }
}