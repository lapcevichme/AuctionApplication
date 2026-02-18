package com.lapcevichme.auctionapplication.presentation.features.auth

sealed interface LoginUiState {
    data object Loading : LoginUiState

    data class Content(
        val email: String = "",
        val password: String = "",
        val emailError: String? = null,
        val passwordError: String? = null,
        val isSubmitting: Boolean = false
    ) : LoginUiState {
        val canLogin: Boolean get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting
    }

    data class Error(val message: String) : LoginUiState
}