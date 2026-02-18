package com.lapcevichme.auctionapplication.domain.model

data class User(
    val name: String,
    val role: UserRole,
    val avatarUrl: String?
)