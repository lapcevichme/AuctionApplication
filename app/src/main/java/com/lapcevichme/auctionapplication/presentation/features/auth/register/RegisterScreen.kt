package com.lapcevichme.auctionapplication.presentation.features.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lapcevichme.auctionapplication.presentation.features.auth.AuthTextField

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> onRegisterSuccess()
                is RegisterEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            RegisterContent(
                state = state,
                onEmailChange = viewModel::onEmailChanged,
                onNameChange = viewModel::onNameChanged,
                onPasswordChange = viewModel::onPasswordChanged,
                onRegisterClick = viewModel::onRegisterClick,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
fun RegisterContent(
    state: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

        if (state.generalError != null) {
            Text(
                text = state.generalError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        AuthTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = state.emailError != null,
            errorText = state.emailError,
            enabled = !state.isSubmitting
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = "Имя",
            isError = state.nameError != null,
            errorText = state.nameError,
            enabled = !state.isSubmitting
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = "Пароль",
            isPassword = true,
            isError = state.passwordError != null,
            errorText = state.passwordError,
            enabled = !state.isSubmitting
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRegisterClick,
            enabled = state.canRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onLoginClick,
            enabled = !state.isSubmitting
        ) {
            Text("Войти", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    MaterialTheme {
        RegisterContent(
            state = RegisterUiState(
                email = "newuser@example.com",
                name = "Alex",
                isSubmitting = false
            ),
            onEmailChange = {},
            onNameChange = {},
            onPasswordChange = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}