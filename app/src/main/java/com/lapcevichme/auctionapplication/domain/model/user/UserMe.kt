package com.lapcevichme.auctionapplication.domain.model.user

data class UserMe(
    val email: String,
    val name: String,
    val role: UserRole,
    val avatarUrl: String?,
    val balance: UserBalance
)