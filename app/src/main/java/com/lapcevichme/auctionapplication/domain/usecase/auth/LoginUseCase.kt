package com.lapcevichme.auctionapplication.domain.usecase.auth

import com.lapcevichme.auctionapplication.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.login(email, password)
    }
}