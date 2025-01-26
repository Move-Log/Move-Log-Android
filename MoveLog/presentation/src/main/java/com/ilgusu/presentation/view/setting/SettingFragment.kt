package com.ilgusu.presentation.view.setting

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentSettingBinding
import androidx.fragment.app.viewModels
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()
    private val serviceUrl =
        "https://amusing-consonant-8d3.notion.site/172968a00fd180f09f15eea77cd519a8?pvs=4"
    private val privacyUrl =
        "https://amusing-consonant-8d3.notion.site/16f968a00fd180d2b951d7d1c8604be1?pvs=4"

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(NavigationCommand.Back)
            }
        }
        binding.tvAboutService.setOnClickListener {
            moveInternet(serviceUrl)
        }
        binding.tvAboutPrivacy.setOnClickListener {
            moveInternet(privacyUrl)
        }
        binding.btnAboutService.setOnClickListener {
            moveInternet(serviceUrl)
        }
        binding.btnAboutPrivacy.setOnClickListener {
            moveInternet(privacyUrl)
        }

        binding.btnDeleteAccount.setOnClickListener {
            viewModel.withdraw()
        }
    }

    private fun moveInternet(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    lifecycleScope.launch {
                        navigationManager.navigate(
                            NavigationCommand.ToRouteAndClear(NavigationRoutes.SignIn)
                        )
                    }
                }
            }
        }
    }
}