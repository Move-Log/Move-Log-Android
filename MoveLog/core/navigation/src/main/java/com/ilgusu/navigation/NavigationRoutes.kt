package com.ilgusu.navigation

sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes("home")
    data object SignIn : NavigationRoutes("sign_in")
    data object Term: NavigationRoutes("terms")
}