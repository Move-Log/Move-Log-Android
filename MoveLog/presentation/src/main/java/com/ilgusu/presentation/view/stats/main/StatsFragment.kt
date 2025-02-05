package com.ilgusu.presentation.view.stats.main

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentStatsBinding
import com.ilgusu.presentation.util.OnClickRvItemListener
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : BaseFragment<FragmentStatsBinding>() {

    private val viewModel: StatsViewModel by viewModels()
    private lateinit var rvAdapter: AllStatsRankRvAdapter
    private lateinit var chips: List<TextView>
    private lateinit var categories: List<TextView>

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
        setRvAdapter()
        viewModel.fetchData()

        chips = listOf(
            binding.tvChipTotal, binding.tvChipDaily, binding.tvChipWeekly, binding.tvChipMonthly
        )

        categories = listOf(
            binding.tvDo, binding.tvGo, binding.tvEat
        )
    }

    private fun setBottomNav() {
        binding.bottomNav.ivChart.setImageResource(R.drawable.ic_chart_enabled)
        binding.bottomNav.tvChart.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_1c
            )
        )

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

    private fun setRvAdapter() {
        rvAdapter = AllStatsRankRvAdapter().apply {
            setOnRvItemClickListener(object : OnClickRvItemListener<String> {
                override fun onClick(item: String) {

                }
            })
        }

        binding.rvRank.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
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

        chips.forEach { tv ->
            tv.setOnClickListener {
                viewModel.setPeriodState(tv.text.toString())

                chips.forEach { chip ->
                    val colors = if (viewModel.periodState.value == chip.text) {
                        Pair(R.color.white, R.color.gray_35)
                    } else {
                        Pair(R.color.gray_4d, R.color.gray_e1)
                    }

                    chip.setTextColor(ContextCompat.getColor(requireContext(), colors.first))
                    chip.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                colors.second
                            )
                        )
                }
            }
        }

        categories.forEach { tv ->
            tv.setOnClickListener {
                viewModel.setCategoryState(tv.text.toString())

                categories.forEach { category ->
                    val textColor = if (viewModel.categoryState.value == category.text) {
                        R.color.gray_1c
                    } else {
                        R.color.gray_7f
                    }

                    category.setTextColor(ContextCompat.getColor(requireContext(), textColor))
                }
            }
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> showToast(it.message)
                is UiState.Success -> {
                    binding.tvTotalCount.text = changeTextColor(it.data.totalRecords.toString())
                    binding.tvAvgDailyCount.text =
                        changeTextColor(it.data.avgDailyRecord.toString())
                    binding.tvMaxDailyCount.text =
                        changeTextColor(it.data.maxDailyRecord.toString())
                    binding.tvMaxConsecutiveCount.text =
                        changeTextColor(it.data.maxConsecutiveDays.toString())

                    rvAdapter.submitList(it.data.topRecords)
                }
            }
        }

        viewModel.categoryState.observe(viewLifecycleOwner) {
            viewModel.fetchData()
        }

        viewModel.periodState.observe(viewLifecycleOwner) {
            viewModel.fetchData()
        }
    }

    private fun changeTextColor(text: String) = buildSpannedString {
        color(ContextCompat.getColor(requireContext(), R.color.primary)) {
            append(text)
        }
        append("건")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }
}