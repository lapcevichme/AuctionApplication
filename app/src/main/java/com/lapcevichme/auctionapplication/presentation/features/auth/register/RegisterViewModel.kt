package com.lapcevichme.auctionapplication.presentation.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RegisterEvent {
    data object RegisterSuccess : RegisterEvent
    data class ShowError(val message: String) : RegisterEvent
}

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<RegisterEvent>()
    val events = _events.receiveAsFlow()

    fun onEmailChanged(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = null, generalError = null)
        }
    }

    fun onNameChanged(value: String) {
        _uiState.update {
            it.copy(name = value, nameError = null, generalError = null)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(password = value, passwordError = null, generalError = null)
        }
    }

    fun onRegisterClick() {
        val currentState = _uiState.value

        if (currentState.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Пароль слишком короткий (минимум 6)") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, generalError = null) }

            val result = registerUseCase(
                email = currentState.email,
                name = currentState.name,
                password = currentState.password
            )

            result.onSuccess {
                _uiState.update { it.copy(isSubmitting = false) }
                _events.send(RegisterEvent.RegisterSuccess)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        emailError = if (error.message?.contains(
                                "exists",
                                ignoreCase = true
                            ) == true
                        ) "Email уже занят" else null,
                        generalError = error.message ?: "Ошибка регистрации"
                    )
                }
                _events.send(RegisterEvent.ShowError(error.message ?: "Registration failed"))
            }
        }
    }
}