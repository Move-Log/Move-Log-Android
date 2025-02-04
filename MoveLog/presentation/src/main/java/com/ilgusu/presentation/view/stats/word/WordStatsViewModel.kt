package com.ilgusu.presentation.view.stats.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.stats.WordIdStats
import com.ilgusu.domain.model.stats.WordStats
import com.ilgusu.domain.usecase.stats.GetAllWordStatsUseCase
import com.ilgusu.domain.usecase.stats.GetMyWordStatsUseCase
import com.ilgusu.domain.usecase.stats.GetRecentRecordWordsUseCase
import com.ilgusu.domain.usecase.stats.SearchWordsUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordStatsViewModel @Inject constructor(
    private val getRecentRecordWordsUseCase: GetRecentRecordWordsUseCase,
    private val searchWordsUseCase: SearchWordsUseCase
) : ViewModel() {
    private val _getRecentRecordWordsState = MutableLiveData<UiState<List<WordIdStats>>>()
    private val _searchWordsState = MutableLiveData<UiState<List<WordIdStats>>>()

    val getRecentRecordWordState : LiveData<UiState<List<WordIdStats>>> = _getRecentRecordWordsState
    val searchWordState :LiveData<UiState<List<WordIdStats>>> = _searchWordsState

    fun fetchRecentRecordWords() {
        _getRecentRecordWordsState.value = UiState.Loading
        viewModelScope.launch {
            getRecentRecordWordsUseCase.invoke()
                .onSuccess {
                    _getRecentRecordWordsState.value = UiState.Success(it)
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _getRecentRecordWordsState.value = UiState.Error(it.message.toString())
                }
        }
    }

    fun searchWord(keyword: String) {
        _searchWordsState.value = UiState.Loading
        viewModelScope.launch {
            searchWordsUseCase.invoke(keyword)
                .onSuccess {
                    _searchWordsState.value = UiState.Success(it)
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _searchWordsState.value = UiState.Error(it.message.toString())
                }
        }
    }
}