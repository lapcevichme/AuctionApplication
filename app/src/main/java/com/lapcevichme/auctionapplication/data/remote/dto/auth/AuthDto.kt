package com.lapcevichme.auctionapplication.data.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.lapcevichme.auctionapplication.domain.model.AuthTokens

@Serializable
data class AuthResponseDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("expiresIn") val expiresIn: Long = 3600
)

fun AuthResponseDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        expiresAt = System.currentTimeMillis() + (this.expiresIn * 1000)
    )
}