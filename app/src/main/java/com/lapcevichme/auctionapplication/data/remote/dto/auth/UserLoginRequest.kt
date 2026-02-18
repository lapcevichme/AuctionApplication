package com.lapcevichme.auctionapplication.data.remote.dto.auth

data class UserLoginRequest(
    val email: String,
    val password: String
)