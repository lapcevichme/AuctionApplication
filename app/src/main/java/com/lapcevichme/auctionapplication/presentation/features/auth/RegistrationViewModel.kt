package com.lapcevichme.auctionapplication.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RegistrationEvent {
    data object RegistrationSuccess : RegistrationEvent
    data class ShowError(val message: String) : RegistrationEvent
}

class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Content())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<RegistrationEvent>()
    val events = _events.receiveAsFlow()

    fun onEmailChanged(value: String) {
        updateContent { it.copy(email = value, emailError = null) }
    }

    fun onNameChanged(value: String) {
        updateContent { it.copy(name = value, nameError = null) }
    }

    fun onPasswordChanged(value: String) {
        updateContent { it.copy(password = value, passwordError = null) }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value as? RegistrationUiState.Content ?: return

        if (currentState.password.length < 6) {
            updateContent { it.copy(passwordError = "Пароль слишком короткий") }
            return
        }

        viewModelScope.launch {
            updateContent { it.copy(isSubmitting = true) }

            val result = registerUseCase(
                email = currentState.email,
                name = currentState.name,
                password = currentState.password
            )

            result.onSuccess {
                _events.send(RegistrationEvent.RegistrationSuccess)
                updateContent { it.copy(isSubmitting = false) }
            }.onFailure { error ->
                updateContent {
                    it.copy(
                        isSubmitting = false,
                        emailError = if (error.message?.contains("exists") == true) "Email занят" else null
                    )
                }
                _events.send(RegistrationEvent.ShowError(error.message ?: "Registration failed"))
            }
        }
    }

    private fun updateContent(update: (RegistrationUiState.Content) -> RegistrationUiState.Content) {
        _uiState.update { currentState ->
            if (currentState is RegistrationUiState.Content) {
                update(currentState)
            } else {
                currentState
            }
        }
    }
}