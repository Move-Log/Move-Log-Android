package com.ilgusu.navigation

sealed class NavigationCommand {
    data class ToRoute(val route: NavigationRoutes) : NavigationCommand()
    data object Back : NavigationCommand()
    data class PopUpTo(val route: NavigationRoutes, val inclusive: Boolean) : NavigationCommand()
}