package com.lapcevichme.auctionapplication.domain.model

data class AuthTokens(
    val accessToken: String?,
    val refreshToken: String?,
    val expiresAt: Long? = null
)