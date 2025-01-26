package com.ilgusu.navigation

import android.os.Bundle

sealed class NavigationCommand {
    data class ToRoute(val route: NavigationRoutes) : NavigationCommand()
    data class ToRouteWithId(val id: Int, val bundle: Bundle? = null) : NavigationCommand()
    data object Back : NavigationCommand()
    data class PopUpTo(val route: NavigationRoutes, val inclusive: Boolean) : NavigationCommand()
    data class ToRouteAndClear(val route: NavigationRoutes) : NavigationCommand()
}