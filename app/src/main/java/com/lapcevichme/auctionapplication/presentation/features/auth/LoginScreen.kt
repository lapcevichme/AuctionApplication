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
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        when (state) {
            is LoginUiState.Loading -> CircularProgressIndicator()
            is LoginUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            is LoginUiState.Content -> {
                AuthTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    isError = state.emailError != null,
                    errorText = state.emailError
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
                    text = if (state.isSubmitting) "Загрузка..." else "Войти",
                    onClick = onLoginClick,
                    enabled = state.canLogin
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onRegisterClick) {
            Text("Зарегистрироваться", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginUiState.Content(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}