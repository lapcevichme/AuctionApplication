package com.lapcevichme.auctionapplication.di

import android.content.Context
import android.util.Log
import com.lapcevichme.auctionapplication.data.local.TokenStorage
import com.lapcevichme.auctionapplication.data.remote.dto.auth.AuthResponseDto
import com.lapcevichme.auctionapplication.data.remote.dto.auth.RefreshRequest
import com.lapcevichme.auctionapplication.data.remote.dto.auth.toDomain
import com.lapcevichme.auctionapplication.data.repository.AuthRepositoryImpl
import com.lapcevichme.auctionapplication.domain.usecase.auth.LoginCheckUseCase
import com.lapcevichme.auctionapplication.domain.usecase.auth.LoginUseCase
import com.lapcevichme.auctionapplication.domain.usecase.auth.RegisterUseCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private const val BASE_URL = "http://cmdev.pw:8089/"

    private val jsonParams = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }


    val tokenStorage by lazy {
        TokenStorage(applicationContext)
    }

    val authClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(jsonParams)
            }
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor-Auth", message)
                    }
                }
                level = LogLevel.BODY
            }
        }
    }

    val httpClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(jsonParams)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor-Main", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokens = tokenStorage.getTokens()
                        if (tokens.accessToken.isNullOrBlank() || tokens.refreshToken.isNullOrBlank()) {
                            null
                        } else {
                            BearerTokens(tokens.accessToken, tokens.refreshToken)
                        }
                    }

                    refreshTokens {
                        val oldTokens = tokenStorage.getTokens()
                        val refreshToken = oldTokens.refreshToken
                        if (refreshToken.isNullOrBlank()) return@refreshTokens null

                        try {
                            val responseDto = authClient.post("auth/refresh") {
                                setBody(RefreshRequest(refreshToken))
                            }.body<AuthResponseDto>()

                            val newTokens = responseDto.toDomain()
                            tokenStorage.saveTokens(newTokens)

                            BearerTokens(newTokens.accessToken!!, newTokens.refreshToken!!)
                        } catch (e: ClientRequestException) {
                            Log.e("Auth", "Refresh token is invalid/expired. Logging out.")
                            tokenStorage.clear()
                            null
                        } catch (e: Exception) {
                            Log.e("Auth", "Network/Server error during refresh: ${e.message}")
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        path.contains("/auth") || path.contains("login") || path.contains("register")
                    }
                }
            }
        }
    }

    val authRepository by lazy {
        AuthRepositoryImpl(authClient, tokenStorage)
    }

    val loginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val registerUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    val loginCheckUseCase by lazy {
        LoginCheckUseCase(authRepository)
    }

    /*
    val mainRepository by lazy {
        MainRepositoryImpl(httpClient)
    }
    */
}