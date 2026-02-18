package com.lapcevichme.auctionapplication.domain.usecase.auth

import com.lapcevichme.auctionapplication.domain.repository.AuthRepository

class LoginCheckUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}