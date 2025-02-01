package com.ilgusu.presentation.view.news.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.RecordCalendarContent
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.domain.usecase.news.GetDateNewsRecordUseCase
import com.ilgusu.domain.usecase.news.GetTodayNewsRecordUseCase
import com.ilgusu.domain.usecase.record.GetTodayRecordListUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsCalendarViewModel @Inject constructor(
    private val getDateNewsRecordUseCase: GetDateNewsRecordUseCase
) : ViewModel() {
    private val _monthState = MutableLiveData<UiState<List<NewsContent>>>()
    val monthState: LiveData<UiState<List<NewsContent>>> get() = _monthState

    private var page = 0
    private var pageIsFinish = false
    private var lastDate = ""

    fun fetchData(date: String) {
        if (lastDate != date && date.isNotBlank()) {
            pageIsFinish = false
            page = 0
        }

        if (pageIsFinish) {
            return
        }
        _monthState.value = UiState.Loading
        viewModelScope.launch {
            getDateNewsRecordUseCase.invoke(date.ifBlank { lastDate }, page)
                .onSuccess {
                    _monthState.value = UiState.Success(it.content)
                    page++
                    if(date.isNotBlank()) lastDate = date
                    pageIsFinish = it.last
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _monthState.value = UiState.Error(it.message.toString())
                }
        }
    }
}