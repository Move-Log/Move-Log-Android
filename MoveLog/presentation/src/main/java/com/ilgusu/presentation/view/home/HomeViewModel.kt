package com.ilgusu.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.record.GetRecentCurrentImagesUseCase
import com.ilgusu.domain.usecase.record.GetTodayRecordUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayRecordUseCase: GetTodayRecordUseCase,
    private val getRecentCurrentImagesUseCase: GetRecentCurrentImagesUseCase
): ViewModel() {

    private val _recordState = MutableLiveData<UiState<List<Int>>>(UiState.Loading)
    val recordState: LiveData<UiState<List<Int>>> get() = _recordState

    private val _currentImageState = MutableLiveData<UiState<List<String>>>()
    val currentImageState: LiveData<UiState<List<String>>> get() = _currentImageState

    init {
        fetchCurrentImages()
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

    private fun fetchCurrentImages(){
        _currentImageState.value = UiState.Loading

        viewModelScope.launch {
            getRecentCurrentImagesUseCase.invoke()
                .onSuccess { _currentImageState.value = UiState.Success(it) }
                .onFailure { _currentImageState.value = UiState.Error(it.message.toString()) }
        }
    }
}