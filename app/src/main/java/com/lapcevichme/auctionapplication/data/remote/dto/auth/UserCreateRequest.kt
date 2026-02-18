package com.lapcevichme.auctionapplication.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val email: String,
    val name: String,
    val password: String
)