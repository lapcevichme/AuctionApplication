package com.lapcevichme.auctionapplication.presentation.features.auth

data class RegisterUiState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val emailError: String? = null,
    val nameError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isSubmitting: Boolean = false
) {
    val canRegister: Boolean
        get() = email.isNotBlank() &&
                name.isNotBlank() &&
                password.isNotBlank() &&
                !isSubmitting
}