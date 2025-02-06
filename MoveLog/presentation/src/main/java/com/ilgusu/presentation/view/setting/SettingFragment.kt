package com.ilgusu.presentation.view.setting

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.BuildConfig
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentSettingBinding
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()
    override fun initView() {}

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(NavigationCommand.Back)
            }
        }
        binding.tvAboutService.setOnClickListener {
            moveInternet(BuildConfig.SERVICE_URL)
        }
        binding.tvAboutPrivacy.setOnClickListener {
            moveInternet(BuildConfig.PRIVACY_URL)
        }
        binding.btnAboutService.setOnClickListener {
            moveInternet(BuildConfig.SERVICE_URL)
        }
        binding.btnAboutPrivacy.setOnClickListener {
            moveInternet(BuildConfig.PRIVACY_URL)
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
                    showToast(it.message, 2)
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