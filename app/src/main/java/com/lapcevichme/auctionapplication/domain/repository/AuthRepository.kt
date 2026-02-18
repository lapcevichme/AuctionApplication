package com.lapcevichme.auctionapplication.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String) : Result<Unit>
    suspend fun register(email: String, name: String, password: String) : Result<Unit>
    suspend fun logout()
    fun isLoggedIn() : Boolean
}