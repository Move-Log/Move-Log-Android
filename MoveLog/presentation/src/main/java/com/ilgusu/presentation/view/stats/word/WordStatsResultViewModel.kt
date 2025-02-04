package com.ilgusu.presentation.view.stats.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.stats.WordStats
import com.ilgusu.domain.usecase.stats.GetAllWordStatsUseCase
import com.ilgusu.domain.usecase.stats.GetMyWordStatsUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordStatsResultViewModel @Inject constructor(
    private val getAllWordStatsUseCase: GetAllWordStatsUseCase,
    private val getMyWordStatsUseCase: GetMyWordStatsUseCase
) : ViewModel() {
    private val _getAllWordStatsState = MutableLiveData<UiState<WordStats>>()
    private val _getMyWordStatsState = MutableLiveData<UiState<WordStats>>()
    private val _tabState = MutableLiveData<Boolean>(false)

    val getAllWordStatsState: LiveData<UiState<WordStats>> = _getAllWordStatsState
    val getMyWordStatsState: LiveData<UiState<WordStats>> = _getMyWordStatsState
    val tabState: LiveData<Boolean> = _tabState

    var allWordData: WordStats? = null
    var myWordData: WordStats? = null

    fun fetchAllWord(keyword: String) {
        _getAllWordStatsState.value = UiState.Loading
        viewModelScope.launch {
            getAllWordStatsUseCase.invoke(keyword)
                .onSuccess {
                    allWordData = it
                    _getAllWordStatsState.value = UiState.Success(it)
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _getMyWordStatsState.value = UiState.Error(it.message.toString())
                }
        }
    }

    fun fetchMyWord(keywordId: Int) {
        _getMyWordStatsState.value = UiState.Loading
        viewModelScope.launch {
            getMyWordStatsUseCase.invoke(keywordId)
                .onSuccess {
                    myWordData = it
                    _getMyWordStatsState.value = UiState.Success(it)
                }
                .onFailure {
                    LoggerUtil.e(it.message.toString(), it)
                    _getMyWordStatsState.value = UiState.Error(it.message.toString())
                }
        }
    }

    fun changeTab(boolean: Boolean) {
        _tabState.value = boolean
    }
}