package com.lapcevichme.auctionapplication.data.remote

import com.lapcevichme.auctionapplication.di.Dependencies
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlin.coroutines.cancellation.CancellationException

@Serializable
data class ErrorResponse(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val path: String? = null
)

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

private suspend fun mapNetworkException(e: Exception): Exception {
    return when (e) {
        is ClientRequestException -> {
            val errorBody = try {
                e.response.bodyAsText()
            } catch (parseEx: Exception) {
                null
            }

            val errorMessage = try {
                if (errorBody != null) {
                    val errorObj = Dependencies.json.decodeFromString<ErrorResponse>(errorBody)
                    errorObj.message ?: errorObj.error ?: "Ошибка ввода данных"
                } else {
                    "Ошибка клиента"
                }
            } catch (jsonEx: Exception) {
                errorBody ?: "Некорректный запрос"
            }

            Exception(errorMessage)
        }

        is ServerResponseException -> {
            Exception("Сервер временно недоступен (${e.response.status.value})")
        }

        is IOException -> {
            Exception("Нет подключения к интернету")
        }

        is JsonConvertException -> {
            Exception("Ошибка обработки данных от сервера")
        }

        else -> {
            Exception("Неизвестная ошибка: ${e.localizedMessage}")
        }
    }
}