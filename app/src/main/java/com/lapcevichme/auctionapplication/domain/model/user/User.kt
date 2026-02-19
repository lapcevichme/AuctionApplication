package com.lapcevichme.auctionapplication.domain.model.user

data class User(
    val id: String,
    val name: String,
    val role: UserRole,
    val avatarUrl: String?
)