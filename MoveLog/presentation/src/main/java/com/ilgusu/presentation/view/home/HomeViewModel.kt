package com.ilgusu.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.news.GetTodayNewsRecordUseCase
import com.ilgusu.domain.usecase.record.GetTodayRecordUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayRecordUseCase: GetTodayRecordUseCase,
    private val getTodayNewsRecordUseCase: GetTodayNewsRecordUseCase
): ViewModel() {

    private val _recordState = MutableLiveData<UiState<List<Int>>>(UiState.Loading)
    private val _newsRecordState = MutableLiveData<UiState<Int>>(UiState.Loading)
    val recordState: LiveData<UiState<List<Int>>> get() = _recordState
    val newsRecordState: LiveData<UiState<Int>> get() = _newsRecordState

    init {
        fetchRecord()
        fetchNewsRecord()
    }

    private fun fetchRecord(){
        _recordState.value = UiState.Loading

        viewModelScope.launch {
            getTodayRecordUseCase.invoke()
                .onSuccess { _recordState.value = UiState.Success(it) }
                .onFailure { _recordState.value = UiState.Error(it.message.toString()) }
        }
    }

    private fun fetchNewsRecord() {
        _newsRecordState.value = UiState.Loading

        viewModelScope.launch {
            getTodayNewsRecordUseCase.invoke()
                .onSuccess { _newsRecordState.value = UiState.Success(it) }
                .onFailure { _newsRecordState.value = UiState.Error(it.message.toString()) }
        }
    }
}