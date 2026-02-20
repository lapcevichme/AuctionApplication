package com.lapcevichme.auctionapplication.data.remote

import com.lapcevichme.auctionapplication.di.Dependencies
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.coroutines.cancellation.CancellationException

@Serializable
data class ApiErrorDto(
    val timestamp: String? = null,
    val status: Int? = null,
    val code: String? = null,
    val message: String? = null,
    val path: String? = null,
    val details: Map<String, JsonElement>? = null
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
                if (!errorBody.isNullOrBlank()) {
                    val errorObj = Dependencies.json.decodeFromString<ApiErrorDto>(errorBody)

                    val detailInfo =
                        errorObj.details?.map { "${it.key}: ${it.value}" }?.joinToString(", ")

                    val baseMessage = errorObj.message ?: errorObj.code ?: "Ошибка данных"

                    if (!detailInfo.isNullOrEmpty()) "$baseMessage ($detailInfo)" else baseMessage
                } else {
                    "Ошибка клиента: ${e.response.status.value}"
                }
            } catch (jsonEx: Exception) {
                errorBody ?: "Ошибка запроса (${e.response.status.value})"
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