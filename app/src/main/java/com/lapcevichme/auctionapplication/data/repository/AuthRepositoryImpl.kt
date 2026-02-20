package com.lapcevichme.auctionapplication.data.repository

import com.lapcevichme.auctionapplication.data.local.TokenStorage
import com.lapcevichme.auctionapplication.data.remote.dto.auth.AuthResponseDto
import com.lapcevichme.auctionapplication.data.remote.dto.auth.UserCreateRequest
import com.lapcevichme.auctionapplication.data.remote.dto.auth.UserLoginRequest
import com.lapcevichme.auctionapplication.data.remote.dto.auth.toDomain
import com.lapcevichme.auctionapplication.data.remote.safeApiCall
import com.lapcevichme.auctionapplication.di.Dependencies
import com.lapcevichme.auctionapplication.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.runBlocking

class AuthRepositoryImpl(
    private val authClient: HttpClient,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        val result = safeApiCall {
            authClient.post("auth/login") {
                setBody(UserLoginRequest(email = email, password = password))
            }.body<AuthResponseDto>()
        }

        return result.fold(
            onSuccess = { dto ->
                tokenStorage.saveTokens(dto.toDomain())
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun register(email: String, name: String, password: String): Result<Unit> {
        val result = safeApiCall {
            authClient.post("auth/register") {
                setBody(UserCreateRequest(email = email, name = name, password = password))
            }.body<AuthResponseDto>()
        }

        return result.fold(
            onSuccess = { dto ->
                tokenStorage.saveTokens(dto.toDomain())
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun logout() {
        safeApiCall {
            Dependencies.httpClient.post("auth/logout")
        }

        tokenStorage.clear()
    }


    override fun isLoggedIn(): Boolean {
        return runBlocking {
            val tokens = tokenStorage.getTokens()
            !tokens.refreshToken.isNullOrBlank()
        }
    }
}