package com.ilgusu.presentation.view.news.recent

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.navigation.NavigationRoutes
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentNewsBinding
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private val viewModel: NewsViewModel by viewModels()
    private val newsRvAdapter: NewsRecentRvAdapter by lazy { NewsRecentRvAdapter() }
    private val chips: List<Triple<TextView, ImageView?, LinearLayout>> by lazy {
        listOf(
            Triple(binding.tvChipAll, null, binding.chipAll),
            Triple(binding.tvChipDo, binding.ivChipDo, binding.chipDo),
            Triple(binding.tvChipGo, binding.ivChipGo, binding.chipGo),
            Triple(binding.tvChipEat, binding.ivChipEat, binding.chipEat)
        )
    }

    private var isLoading = false

    override fun initView() {
        setBottomNav()
        setRvAdapter()

        // 구현 필요
        binding.btnDateNews.visibility = View.INVISIBLE
    }

    override fun initListener() {
        super.initListener()

        binding.btnPostNews.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.NewsCreate)
                )
            }
        }

        chips.forEachIndexed { index, item ->
            item.third.setOnClickListener { setVerbTypeChip(index) }
        }

        binding.rvRecentNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newsRvAdapter.itemCount == 0) return

                if (newState == SCROLL_STATE_IDLE) {
                    binding.btnPostNews.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .withStartAction { binding.btnPostNews.visibility = View.VISIBLE }
                        .start()
                } else {
                    binding.btnPostNews.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction { binding.btnPostNews.visibility = View.GONE }
                        .start()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPos >= itemTotalCount - 5) {
                    if (!isLoading) {
                        isLoading = true
                        viewModel.fetchData()
                    }
                }
            }
        })
    }

    private fun setRvAdapter() {
        binding.rvRecentNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecentNews.adapter = newsRvAdapter
        viewModel.fetchData()
    }

    private fun setVerbTypeChip(verbType: Int) {
        chips[verbType].apply {
            first.setTextColor(Color.WHITE)
            second?.imageTintList = ColorStateList.valueOf(Color.WHITE)
            third.background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_chip)
        }

        chips.forEachIndexed { index, item ->
            if (index != verbType) {
                item.apply {
                    first.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_4d))
                    second?.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray_66
                        )
                    )
                    third.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.shape_chip_disable)
                }
            }
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Error -> {
                    showToast(it.message)
                }

                is UiState.Success -> {
                    newsRvAdapter.submitList(it.data)


                    binding.tvEmptyView.visibility =
                        if (it.data.isEmpty() && newsRvAdapter.itemCount == 0) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                }
            }
        }
    }

    private fun setBottomNav() {
        binding.bottomNav.ivNews.setImageResource(R.drawable.ic_pencil_enabled)
        binding.bottomNav.tvNews.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.gray_1c
            )
        )

        binding.bottomNav.menuHome.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Home)
                )
            }
        }

        binding.bottomNav.menuChart.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.ToRoute(NavigationRoutes.Setting)
                )
            }
        }
    }
}