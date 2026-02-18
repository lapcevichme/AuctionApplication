package com.lapcevichme.auctionapplication.presentation.features.auth.login

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

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = null, generalError = null)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(password = value, passwordError = null, generalError = null)
        }
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        if (!currentState.canLogin) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, generalError = null) }

            val result = loginUseCase(currentState.email, currentState.password)

            result.onSuccess {
                _uiState.update { it.copy(isSubmitting = false) }
                _events.send(LoginEvent.NavigateToHome)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        generalError = error.message ?: "Ошибка входа"
                    )
                }
            }
        }
    }
}