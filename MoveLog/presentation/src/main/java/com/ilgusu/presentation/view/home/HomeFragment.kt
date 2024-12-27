package com.ilgusu.presentation.view.home

import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnNavigate.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.SignIn)
                )
            }
        }
    }
}