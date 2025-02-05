package com.ilgusu.presentation.view.stats.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.stats.AllRecordStats
import com.ilgusu.domain.usecase.stats.GetAllRecordStatsUseCase
import com.ilgusu.presentation.util.DateUtil
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getAllRecordStatsUseCase: GetAllRecordStatsUseCase
):ViewModel() {

    private val _uiState = MutableLiveData<UiState<AllRecordStats>>()
    val uiState: LiveData<UiState<AllRecordStats>> get() = _uiState

    fun fetchData() {
        _uiState.value = UiState.Loading

        val month = if(periodState.value == "월간") DateUtil.getCurrentDate("yyyy-MM") else null
        val period = when (periodState.value) {
            "누적" -> "total"
            "일간" -> "daily"
            "주간" -> "weekly"
            else -> "monthly"
        }
        viewModelScope.launch {
            getAllRecordStatsUseCase.invoke(categoryState.value!!, period, month)
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message.toString()) }
        }
    }

    private val _categoryState = MutableLiveData<String>("했어요")
    val categoryState: LiveData<String> get() = _categoryState

    fun setCategoryState(category: String){
        _categoryState.value = category
    }

    private val _periodState = MutableLiveData<String>("누적")
    val periodState: LiveData<String> get() = _periodState

    fun setPeriodState(period: String){
        _periodState.value = period
    }
}