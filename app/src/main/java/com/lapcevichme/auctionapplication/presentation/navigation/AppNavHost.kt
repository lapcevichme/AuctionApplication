package com.lapcevichme.auctionapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lapcevichme.auctionapplication.di.Dependencies
import com.lapcevichme.auctionapplication.presentation.features.auth.LoginScreen
import com.lapcevichme.auctionapplication.presentation.features.auth.LoginViewModel
import com.lapcevichme.auctionapplication.presentation.features.auth.RegisterScreen
import com.lapcevichme.auctionapplication.presentation.features.auth.RegisterViewModel
import com.lapcevichme.auctionapplication.presentation.features.splash.SplashScreen
import com.lapcevichme.auctionapplication.presentation.features.splash.SplashViewModel
import com.lapcevichme.auctionapplication.presentation.utils.viewModelFactory

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            val viewModel: SplashViewModel = viewModel(
                factory = viewModelFactory {
                    SplashViewModel(Dependencies.loginCheckUseCase)
                }
            )

            SplashScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel(
                factory = viewModelFactory {
                    LoginViewModel(Dependencies.loginUseCase)
                }
            )

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = viewModel(
                factory = viewModelFactory {
                    RegisterViewModel(Dependencies.registerUseCase)
                }
            )

            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Main.route) {
            /*
            val viewModel: MainViewModel = viewModel(
                factory = viewModelFactory {
                    MainViewModel(Dependencies.authRepository)
                }
            )

            MainScreen(viewModel = viewModel)
            */
        }
    }
}