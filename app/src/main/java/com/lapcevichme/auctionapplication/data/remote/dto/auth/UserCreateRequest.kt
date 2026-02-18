package com.lapcevichme.auctionapplication.data.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCreateRequest(
    val email: String,
    @SerialName("shownName") val name: String,
    val password: String
)