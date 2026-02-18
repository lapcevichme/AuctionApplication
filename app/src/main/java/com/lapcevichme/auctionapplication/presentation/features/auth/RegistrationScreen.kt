package com.lapcevichme.auctionapplication.presentation.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationScreen(
    state: RegistrationUiState,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        when (state) {
            is RegistrationUiState.Loading -> CircularProgressIndicator()
            is RegistrationUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            is RegistrationUiState.Content -> {
                AuthTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    isError = state.emailError != null,
                    errorText = state.emailError
                )
                Spacer(modifier = Modifier.height(16.dp))
                AuthTextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    label = "Имя",
                    isError = state.nameError != null,
                    errorText = state.nameError
                )
                Spacer(modifier = Modifier.height(16.dp))
                AuthTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = "Пароль",
                    isPassword = true,
                    isError = state.passwordError != null,
                    errorText = state.passwordError
                )
                Spacer(modifier = Modifier.height(32.dp))
                AuthButton(
                    text = if (state.isSubmitting) "Создание..." else "Зарегистрироваться",
                    onClick = onRegisterClick,
                    enabled = state.canRegister
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onLoginClick) {
            Text("Войти", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationPreview() {
    MaterialTheme {
        RegistrationScreen(
            state = RegistrationUiState.Content(),
            onEmailChange = {},
            onNameChange = {},
            onPasswordChange = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}