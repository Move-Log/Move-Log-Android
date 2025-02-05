package com.ilgusu.presentation.view.stats.word

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.R
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentWordStatsResultBinding
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordStatsResultFragment : BaseFragment<FragmentWordStatsResultBinding>() {

    private val viewModel: WordStatsResultViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun initView() {

        arguments?.let {
            viewModel.fetchMyWord(it.getInt("keywordId"))
            viewModel.fetchAllWord(it.getString("noun") ?: "")
            binding.tvSearchedWord.text = "\"${it.getString("noun")}\""
            binding.tvSearch.text = it.getString("noun")
        }
    }

    override fun initListener() {
        super.initListener()

        binding.tvMyStats.setOnClickListener {
            viewModel.changeTab(false)
            toggleStats(true)
        }
        binding.tvTotalStats.setOnClickListener {
            viewModel.changeTab(true)
            toggleStats(false)
        }

        binding.ivBack.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(NavigationCommand.Back)
            }
        }
    }

    override fun setObserver() {
        viewModel.getMyWordStatsState.observe(viewLifecycleOwner) {
            binding.tvMyStats.performClick()
        }

        viewModel.getAllWordStatsState.observe(viewLifecycleOwner) {
            binding.tvMyStats.performClick()
        }

        viewModel.tabState.observe(viewLifecycleOwner) {

            if (it) {
                if (viewModel.allWordData!!.lastRecordedAt == null) {
                    emptyView(false)
                }
                else {
                    emptyView(true)
                    binding.tvMyRecordCountNum.text =
                        changeTextColor(viewModel.allWordData!!.count.toString())
                    binding.tvAverageRecordNum.text =
                        changeTextColor(viewModel.allWordData!!.avgDailyRecord.toString())
                    binding.tvRecentWeekRecordNum.text =
                        changeTextColor(viewModel.allWordData!!.avgWeeklyRecord.toString())
                    binding.tvLastRecordTimeReal.text = extractDateTime(viewModel.allWordData!!.lastRecordedAt!!)
                }
            } else {
                if (viewModel.myWordData == null) {
                    emptyView(false)
                }
                else {
                    emptyView(true)
                    binding.tvMyRecordCountNum.text =
                        changeTextColor(viewModel.myWordData!!.count.toString())
                    binding.tvAverageRecordNum.text =
                        changeTextColor(viewModel.myWordData!!.avgDailyRecord.toString())
                    binding.tvRecentWeekRecordNum.text =
                        changeTextColor(viewModel.myWordData!!.avgWeeklyRecord.toString())
                    binding.tvLastRecordTimeReal.text = extractDateTime(viewModel.myWordData!!.lastRecordedAt!!)
                }
            }
        }
    }

    private fun toggleStats(isMyStatsSelected: Boolean) {
        binding.underBarMyStats.visibility = if (isMyStatsSelected) View.VISIBLE else View.INVISIBLE
        binding.underBarTotalStats.visibility =
            if (isMyStatsSelected) View.INVISIBLE else View.VISIBLE
    }

    private fun changeTextColor(text: String) = buildSpannedString {
        color(ContextCompat.getColor(requireContext(), R.color.primary)) {
            append(text)
        }
        append("건")
    }

    private fun emptyView(boolean: Boolean) {
        binding.tvEmpty.isVisible = !boolean
        binding.tvMyRecordCount.isVisible = boolean
        binding.tvMyRecordCountNum.isVisible = boolean
        binding.tvAverageRecord.isVisible = boolean
        binding.tvAverageRecordNum.isVisible = boolean
        binding.tvRecentWeekRecord.isVisible = boolean
        binding.tvRecentWeekRecordNum.isVisible = boolean
        binding.tvLastRecordTime.isVisible = boolean
        binding.tvLastRecordTimeReal.isVisible = boolean
    }

    private fun extractDateTime(dateTime: String) : String {
        val newDateTime = dateTime.substring(0, 16)
        return newDateTime.replace("T", " ")
    }
}