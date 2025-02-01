package com.ilgusu.presentation.view.news.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilgusu.presentation.R
import com.ilgusu.presentation.databinding.ItemDayBinding
import com.ilgusu.presentation.view.calendar.DayAdapter
import java.util.Calendar
import java.util.Date

class NewsDayAdapter(
    private val currentMonth: Int,
    private val dayList: MutableList<Date>,
    private val onDateSelected: (Date) -> Unit,
) : RecyclerView.Adapter<NewsDayAdapter.DayView>() {
    private val row = 6
    private var diaryDates: Set<String> = emptySet()
    private var selectedDate: Date? = null

    inner class DayView(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayView(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "ResourceType")
    override fun onBindViewHolder(holder: DayView, position: Int) {
        with(holder.binding) {

            val calendar = Calendar.getInstance()
            calendar.time = dayList[position]
            val dayMonth = calendar.get(Calendar.MONTH) + 1
            tvDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

            val isSelectedDate = isSameDate(dayList[position], selectedDate)

            if (currentMonth != dayMonth) {
                itemDayLayout.isEnabled = false
                // 다른 달의 날짜 안 보이게 표시
                tvDay.visibility = View.INVISIBLE
                ivSelectDate.alpha = 0f
            } else {
                tvDay.visibility = View.VISIBLE
                itemDayLayout.isEnabled = true

                // 선택된 날짜 칠하기
                if (isSelectedDate) {
                    tvDay.setTextColor(holder.itemView.context.getColor(R.color.white))
                    ivSelectDate.alpha = 1f
                    ivSelectDate.setBackgroundResource(R.drawable.ic_selected_date)
                } else {
                    ivSelectDate.alpha = 0f
                    tvDay.setTextColor(Color.BLACK)
                }

                itemDayLayout.setOnClickListener {
                    selectedDate = calendar.time
                    notifyDataSetChanged() // 모든 항목 다시 그리기
                    onDateSelected(calendar.time)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return row * 7
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDiaryDates(diaryDates: Set<String>) {
        this.diaryDates = diaryDates
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedDate(date: Date?) {
        selectedDate = date
        notifyDataSetChanged()
    }

    private fun isSameDate(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}