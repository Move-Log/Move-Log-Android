package com.ilgusu.presentation.view.stats.main

import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment: BaseFragment<FragmentStatsBinding>() {

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRouteAndClear(NavigationRoutes.Home)
                )
            }
        }
    }

    override fun initView() {
        setBottomNav()
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setBottomNav(){
        binding.bottomNav.ivChart.setImageResource(R.drawable.ic_chart_enabled)
        binding.bottomNav.tvChart.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_1c))

        binding.bottomNav.menuNews.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.NewsRecent)
                )
            }
        }

        binding.bottomNav.menuHome.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Home)
                )
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.tvSearch.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.WordStats)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }
}