package com.ilgusu.presentation.view.news.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.usecase.record.SearchRecordUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NounSearchViewModel @Inject constructor(
    private val searchKeywordUseCase: SearchRecordUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<RecommendKeyword>>>()
    val uiState: LiveData<UiState<List<RecommendKeyword>>> get() = _uiState

    fun search(keyword: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            searchKeywordUseCase.invoke(keyword)
                .onFailure { _uiState.value = UiState.Error(it.message.toString()) }
                .onSuccess { _uiState.value = UiState.Success(it) }
        }
    }
}