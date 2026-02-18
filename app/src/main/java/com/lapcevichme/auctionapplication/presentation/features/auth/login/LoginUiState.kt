package com.lapcevichme.auctionapplication.presentation.features.auth.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isSubmitting: Boolean = false,
    val generalError: String? = null
) {
    val canLogin: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isSubmitting
}