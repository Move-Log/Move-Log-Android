package com.ilgusu.presentation.view.signIn

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentSignInBinding
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment: BaseFragment<FragmentSignInBinding>() {

    private val viewModel: SignInViewModel by viewModels()

    override fun initView() {
        requireActivity().window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun initListener() {
        super.initListener()

        binding.llLoginKakao.setOnClickListener {
            doLogin(AuthProvider.KAKAO)
        }

        binding.llLoginGoogle.setOnClickListener {
            doLogin(AuthProvider.GOOGLE)
        }
    }

    private fun doLogin(provider: AuthProvider) {
        viewModel.login(requireActivity(), provider)
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {}
                is UiState.Error -> { showToast(it.message) }
                is UiState.Success -> {
                    if(it.data) {
                        moveToNext(NavigationRoutes.Home)
                    }
                }
            }
        }

        viewModel.loginState.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {}
                is UiState.Error -> { showToast(it.message) }
                is UiState.Success -> {
                    val route = if(it.data) NavigationRoutes.Home else NavigationRoutes.Terms
                    moveToNext(route)
                }
            }
        }
    }

    private fun moveToNext(route: NavigationRoutes){
        lifecycleScope.launch {
            navigationManager.navigate(
                NavigationCommand.ToRouteAndClear(route)
            )
        }
    }
}