package com.ilgusu.movelog

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ilgusu.movelog.databinding.ActivityMainBinding
import com.ilgusu.navigation.AppNavigatorImpl
import com.ilgusu.navigation.NavigationManager
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.apply {
            this.statusBarColor = resources.getColor(com.ilgusu.presentation.R.color.white, null)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        viewModel.uiState.observe(this) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> setNavGraph(false)
                is UiState.Success -> setNavGraph(true)
            }
        }

        viewModel.checkLogin()
    }

    private fun setNavGraph(isAlreadyLogin: Boolean) {
        val navGraph =
            navController.navInflater.inflate(com.ilgusu.presentation.R.navigation.nav_graph)
        navGraph.setStartDestination(
            if (isAlreadyLogin) com.ilgusu.presentation.R.id.homeFragment else com.ilgusu.presentation.R.id.signInFragment
        )
        navController.setGraph(navGraph, null)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navigator = AppNavigatorImpl(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                navigationManager.command.collect { command ->
                    navigator.navigate(command)
                }
            }
        }
    }
}