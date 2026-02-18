package com.lapcevichme.auctionapplication.data.remote.dto.auth

data class UserCreateRequest(
    val email: String,
    val name: String,
    val password: String
)