package com.ilgusu.presentation.view.calendar

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private lateinit var monthAdapter: MonthAdapter
    private lateinit var recordRvAdapter: RecordRvAdapter
    private lateinit var monthListManager: LinearLayoutManager
    private val viewModel: CalendarViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
    private val dateFormatForTv = SimpleDateFormat("yyyy년 MM월 dd일 (EE)", Locale.KOREAN)

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
    }

    @SuppressLint("ClickableViewAccessibility")
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
                    binding.tvIfNoRecord.visibility = if (it.data.isEmpty()) View.VISIBLE else View.GONE
                    recordRvAdapter.submitList(it.data)
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

        monthAdapter = MonthAdapter(0, today)
        monthAdapter.setOnDateSelectedListener(object :
            MonthAdapter.OnDateSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onDateSelected(date: Date) {
                val formattedDate = dateFormat.format(date)

                binding.tvCalendarDate.text = dateFormatForTv.format(date)

                viewModel.fetchData(formattedDate)
            }
        })

        monthListManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.apply {
            layoutManager = monthListManager
            adapter = monthAdapter
            scrollToPosition(Int.MAX_VALUE / 2)

            // 좌우 스크롤 막기
            setOnTouchListener { _, _ -> true }
        }
    }

    private fun setupRecyclerView() {
        // RecordRvAdapter 설정
        recordRvAdapter = RecordRvAdapter()
        binding.rvRecord.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = recordRvAdapter
        }
    }
}