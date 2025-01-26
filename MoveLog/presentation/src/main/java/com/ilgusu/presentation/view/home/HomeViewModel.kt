package com.ilgusu.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.record.GetTodayRecordUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayRecordUseCase: GetTodayRecordUseCase
): ViewModel() {

    private val _recordState = MutableLiveData<UiState<List<Int>>>(UiState.Loading)
    val recordState: LiveData<UiState<List<Int>>> get() = _recordState

    init {
        fetchRecord()
    }

    private fun fetchRecord(){
        _recordState.value = UiState.Loading

        viewModelScope.launch {
            getTodayRecordUseCase.invoke()
                .onSuccess { _recordState.value = UiState.Success(it) }
                .onFailure { _recordState.value = UiState.Error(it.message.toString()) }
        }
    }
}