package com.ilgusu.presentation.view.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilgusu.presentation.util.UiState
import javax.inject.Inject

class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _monthState = MutableLiveData<UiState<Unit>>()
    val monthState: LiveData<UiState<Unit>>get() = _monthState

    fun fetchData() {
        _monthState.value = UiState.Success(Unit)
    }
}