package com.ilgusu.presentation.view.news.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.news.PostNewsUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewsResultViewModel @Inject constructor(
    private val postNewsUseCase: PostNewsUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> get() = _uiState

    private var isFinish: Boolean = false

    fun uploadNews(
        file: File, headline: String, keywordId: Int
    ) {
        if(isFinish) return

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            postNewsUseCase.invoke(keywordId = keywordId, file = file, headLine = headline)
                .onSuccess {
                    isFinish = true
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    _uiState.value = UiState.Error(it.message.toString())
                }
        }
    }
}