package com.ilgusu.presentation.view.news.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.news.NewsContent
import com.ilgusu.domain.usecase.news.GetWeekNewsRecordUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getWeekNewsRecord: GetWeekNewsRecordUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<NewsContent>>>()
    val uiState: LiveData<UiState<List<NewsContent>>> get() = _uiState

    private var page = 0
    private var isFinish = false

    fun fetchData() {
        if(isFinish) return

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            getWeekNewsRecord.invoke(page)
                .onSuccess {
                    if(it.last) isFinish = true
                    page++
                    _uiState.value = UiState.Success(it.content)
                }
                .onFailure {
                    _uiState.value = UiState.Error(it.message.toString())
                }
        }
    }
}