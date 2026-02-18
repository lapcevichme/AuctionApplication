package com.lapcevichme.auctionapplication.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lapcevichme.auctionapplication.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Content())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        updateContent { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChanged(value: String) {
        updateContent { it.copy(password = value, passwordError = null) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value as? LoginUiState.Content ?: return
        if (!currentState.canLogin) return

        viewModelScope.launch {
            updateContent { it.copy(isSubmitting = true) }

            val result = loginUseCase(currentState.email, currentState.password)

            result.onSuccess {
                _events.send(LoginEvent.NavigateToHome)
                updateContent { it.copy(isSubmitting = false) }
            }.onFailure { error ->
                updateContent {
                    it.copy(
                        isSubmitting = false,
                        passwordError = "Ошибка входа. Проверьте данные."
                    )
                }
                _events.send(LoginEvent.ShowError(error.message ?: "Unknown error"))
            }
        }
    }

    private fun updateContent(update: (LoginUiState.Content) -> LoginUiState.Content) {
        _uiState.update { currentState ->
            if (currentState is LoginUiState.Content) {
                update(currentState)
            } else {
                currentState
            }
        }
    }
}