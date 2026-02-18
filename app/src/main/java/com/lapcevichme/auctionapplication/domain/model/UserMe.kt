package com.lapcevichme.auctionapplication.domain.model

data class UserMe(
    val email: String,
    val name: String,
    val role: UserRole,
    val avatarUrl: String?,
    val balance: UserBalance
)