package com.ilgusu.presentation.view.news.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.news.ImageInfo
import com.ilgusu.domain.model.news.RecommendKeyword
import com.ilgusu.domain.usecase.news.CreateNewsHeadLinesUseCase
import com.ilgusu.domain.usecase.news.RecommendNewsKeywordsUseCase
import com.ilgusu.domain.usecase.record.GetRecentRecordImagesUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewsCreateViewModel @Inject constructor(
    private val recommendNewsKeywordsUseCase: RecommendNewsKeywordsUseCase,
    private val getRecentRecordImageUseCase: GetRecentRecordImagesUseCase,
    private val createNewsHeadLinesUseCase: CreateNewsHeadLinesUseCase
) : ViewModel() {

    private val _currentStep = MutableLiveData(1)
    val currentStep: LiveData<Int> get() = _currentStep
    fun setCurrentStep(step: Int) {
        _currentStep.value = step
    }

    private val _selectedKeyword = MutableLiveData<RecommendKeyword?>()
    val selectedKeyword: LiveData<RecommendKeyword?> get() = _selectedKeyword
    fun setKeyword(keyword: RecommendKeyword?) {
        _selectedKeyword.value = keyword
    }

    private val _headlineType = MutableLiveData<String?>()
    val headlineType: LiveData<String?> get() = _headlineType
    fun setHeadlineType(type: String?) {
        _headlineType.value = type
    }

    private val _selectedFile = MutableLiveData<File?>()
    val selectedFile: LiveData<File?> get() = _selectedFile
    fun setFile(file: File?) {
        _selectedFile.value = file
    }

    private val _selectedHeadline = MutableLiveData<String?>()
    val selectedHeadline: LiveData<String?> get() = _selectedHeadline
    fun setHeadline(headline: String?) {
        _selectedHeadline.value = headline
    }

    fun resetData() {
        setKeyword(null)
        setHeadlineType(null)
        setHeadline(null)
        setFile(null)
    }

    private val _recommendKeywordState = MutableLiveData<UiState<List<RecommendKeyword>>>()
    val recommendKeywordState: LiveData<UiState<List<RecommendKeyword>>> get() = _recommendKeywordState

    private val _recentImageState = MutableLiveData<UiState<List<ImageInfo>>>()
    val recentImageInfo: LiveData<UiState<List<ImageInfo>>> get() = _recentImageState

    private val _headlineState = MutableLiveData<UiState<List<String>>>()
    val headlineState: LiveData<UiState<List<String>>> get() = _headlineState

    fun recommendKeyword() {
        _recommendKeywordState.value = UiState.Loading

        viewModelScope.launch {
            recommendNewsKeywordsUseCase.invoke()
                .onFailure { _recommendKeywordState.value = UiState.Error(it.message.toString()) }
                .onSuccess { _recommendKeywordState.value = UiState.Success(it) }
        }
    }

    fun getRecentImages(keywordId: Int? = selectedKeyword.value?.keywordId) {
        if(keywordId == null) return
        _recentImageState.value = UiState.Loading

        viewModelScope.launch {
            getRecentRecordImageUseCase.invoke(keywordId)
                .onFailure { _recentImageState.value = UiState.Error(it.message.toString()) }
                .onSuccess { _recentImageState.value = UiState.Success(it) }
        }
    }

    fun recommendHeadlines(
        keywordId: Int? = selectedKeyword.value?.keywordId,
        option: String? = headlineType.value
    ) {
        if(keywordId == null || option == null) return

        _headlineState.value = UiState.Loading

        viewModelScope.launch {
            createNewsHeadLinesUseCase.invoke(keywordId, option)
                .onFailure { _headlineState.value = UiState.Error(it.message.toString()) }
                .onSuccess { _headlineState.value = UiState.Success(it) }
        }
    }
}