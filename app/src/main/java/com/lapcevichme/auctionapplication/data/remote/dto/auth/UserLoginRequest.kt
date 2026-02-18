package com.lapcevichme.auctionapplication.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    val email: String,
    val password: String
)