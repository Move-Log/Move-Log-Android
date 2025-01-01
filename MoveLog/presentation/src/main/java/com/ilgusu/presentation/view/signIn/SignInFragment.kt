package com.ilgusu.presentation.view.signIn

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
    }

    override fun initListener() {
        super.initListener()

        binding.btnNavigate.setOnClickListener {
//            lifecycleScope.launch {
//                navigationManager.navigate(
//                    NavigationCommand.ToRoute(NavigationRoutes.Home)
//                )
//            }

            viewModel.login(AuthProvider.KAKAO)
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.loginState.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {}
                is UiState.Error -> { showToast(it.message) }
                is UiState.Success -> { showToast(it.data) }
            }
        }
    }
}