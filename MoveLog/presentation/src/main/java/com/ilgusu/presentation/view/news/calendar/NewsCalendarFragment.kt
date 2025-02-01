package com.ilgusu.presentation.view.news.calendar

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.ilgusu.navigation.NavigationCommand
import com.ilgusu.presentation.base.BaseFragment
import com.ilgusu.presentation.databinding.FragmentCalendarBinding
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class NewsCalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private lateinit var monthAdapter: NewsMonthAdapter
    private lateinit var newsRvAdapter: NewsRecordRvAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private val viewModel: NewsCalendarViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
    private val dateFormatForTv = SimpleDateFormat("yyyy년 MM월 dd일 (EE)", Locale.KOREAN)

    private var isLoading = false

    override fun initView() {
        viewModel.fetchData(dateFormat.format(Date()))
        binding.tvCalendarDate.text = dateFormatForTv.format(Date())
        setupRecyclerView()
        setCalendar()
        updateMonthDisplay()
    }

    override fun initListener() {
        super.initListener()

        initCalendarListener()
        binding.imgLeftArrow.setOnClickListener {
            lifecycleScope.launch {
                navigationManager.navigate(
                    NavigationCommand.Back
                )
            }
        }

        binding.rvRecord.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount ?: 0

                if (lastVisibleItemPos >= itemTotalCount - 5) {
                    if (!isLoading) {
                        isLoading = true
                        viewModel.fetchData("")
                    }
                }
            }
        })
    }

    private fun initCalendarListener() {
        binding.ivPrevMonth.setOnClickListener {
            updateMonth(-1)
            monthAdapter.updateCurrentMonth(-1)
        }
        binding.ivNextMonth.setOnClickListener {
            updateMonth(1)
            monthAdapter.updateCurrentMonth(1)
        }
    }

    override fun setObserver() {
        viewModel.monthState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Error -> LoggerUtil.e("해당 달 정보 조회 실패: ${it.message}")
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.tvIfNoRecord.visibility =
                        if (it.data.isEmpty()) View.VISIBLE else View.GONE
                    newsRvAdapter.submitList(it.data)
                    isLoading = false
                }
            }
        }
    }

    private fun updateMonth(monthChange: Int) {
        calendar.add(Calendar.MONTH, monthChange)
        updateMonthDisplay()
    }

    private fun updateMonthDisplay() {
        val dateFormat = SimpleDateFormat("yyyy년 M월", Locale.KOREAN)
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvCurrentMonth.text = formattedDate
    }

    private fun setCalendar() {
        val today = Calendar.getInstance().time

        monthAdapter = NewsMonthAdapter(0, today)
        monthAdapter.setOnDateSelectedListener(object :
            NewsMonthAdapter.OnDateSelectedListener {

            override fun onDateSelected(date: Date) {
                val formattedDate = dateFormat.format(date)
                val dateFormat = dateFormatForTv.format(date)
                if(binding.tvCalendarDate.text != dateFormat) newsRvAdapter.submitList(emptyList(), true)
                binding.tvCalendarDate.text = dateFormat

                viewModel.fetchData(formattedDate)
            }
        })

        monthListManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.apply {
            layoutManager = monthListManager
            adapter = monthAdapter
            scrollToPosition(Int.MAX_VALUE / 2)

            setOnTouchListener { _, _ -> true }
        }
    }

    private fun setupRecyclerView() {
        newsRvAdapter = NewsRecordRvAdapter()
        binding.rvRecord.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = newsRvAdapter
        }
    }
}