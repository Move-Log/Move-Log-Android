package com.ilgusu.navigation

sealed class NavigationRoutes(val route: String) {
    data object Home : NavigationRoutes("home")
    data object SignIn : NavigationRoutes("sign_in")
    data object Terms : NavigationRoutes("terms")
    data object Record : NavigationRoutes("record")
    data object Setting : NavigationRoutes("setting")
    data object NewsRecent : NavigationRoutes("news_recent")
    data object NewsCreate : NavigationRoutes("news_create")
    data object NewsResult : NavigationRoutes("news_result")
}