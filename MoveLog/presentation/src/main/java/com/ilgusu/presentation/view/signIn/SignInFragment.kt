package com.ilgusu.presentation.view.signIn

import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment: BaseFragment<FragmentSignInBinding>() {

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnNavigate.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Home)
                )
            }
        }
    }
}