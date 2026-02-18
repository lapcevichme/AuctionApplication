package com.lapcevichme.auctionapplication.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen(Routes.LOGIN)
    object Register : Screen(Routes.REGISTER)
    object Splash : Screen(Routes.SPLASH)
    object Main : Screen(Routes.MAIN)
    object Routes {
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val SPLASH = "splash"
        const val MAIN = "main"
    }
}