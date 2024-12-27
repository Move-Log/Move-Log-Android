package com.ilgusu.navigation

import androidx.navigation.NavController
import com.ilgusu.navigation.extension.printBackStack
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(
    private val navController: NavController
) : Navigator {

    override fun navigate(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.ToRoute -> {
                navController.navigate(command.route.route)
            }
            is NavigationCommand.Back -> navController.navigateUp()
            is NavigationCommand.PopUpTo -> {
                navController.popBackStack(command.route.route, command.inclusive)
            }
        }

        navController.printBackStack()
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun popUpTo(route: String, inclusive: Boolean) {
        navController.popBackStack(route, inclusive)
    }
}