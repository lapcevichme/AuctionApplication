package com.lapcevichme.auctionapplication.presentation.features.auth

sealed interface RegistrationUiState {
    data object Loading : RegistrationUiState

    data class Content(
        val email: String = "",
        val name: String = "",
        val password: String = "",
        val emailError: String? = null,
        val nameError: String? = null,
        val passwordError: String? = null,
        val isSubmitting: Boolean = false
    ) : RegistrationUiState {
        val canRegister: Boolean get() = email.isNotBlank() &&
                name.isNotBlank() &&
                password.isNotBlank() &&
                !isSubmitting
    }

    data class Error(val message: String) : RegistrationUiState
}