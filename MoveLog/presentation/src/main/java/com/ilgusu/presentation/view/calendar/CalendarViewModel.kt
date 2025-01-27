package com.ilgusu.presentation.view.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.domain.usecase.record.GetTodayRecordListUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTodayRecordListUseCase: GetTodayRecordListUseCase
) : ViewModel() {
    private val _monthState = MutableLiveData<UiState<List<RecordCalendarContent>>>()
    val monthState: LiveData<UiState<List<RecordCalendarContent>>> get() = _monthState

    private var page = 0
    private var pageIsFinish = false
    private val lastDate = ""

    fun fetchData(date: String) {
        if (lastDate != date) {
            pageIsFinish = false
            page = 0
        }

        if (pageIsFinish) {
            return
        }
        _monthState.value = UiState.Loading
        viewModelScope.launch {
            getTodayRecordListUseCase.invoke(date, page)
                .onSuccess {
                    _monthState.value = UiState.Success(it.content)
                    page++
                    pageIsFinish = it.last
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _monthState.value = UiState.Error(it.message.toString())
                }
        }
    }
}