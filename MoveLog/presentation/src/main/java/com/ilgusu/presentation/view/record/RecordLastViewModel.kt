package com.ilgusu.presentation.view.record

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.record.RecordUseCase
import com.ilgusu.presentation.util.ImageUtil
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordLastViewModel @Inject constructor(
    private val recordUseCase: RecordUseCase
) : ViewModel() {

    private val _recordWord = MutableLiveData<String>()
    val recordWord: LiveData<String> get() = _recordWord

    private val _recordType = MutableLiveData<Int>()
    val recordType: LiveData<Int> get() = _recordType

    fun setRecordWord(word: String) {
        _recordWord.value = word
    }

    fun setRecordType(type: Int) {
        _recordType.value = type
    }

    var imageFile: File? = null

    fun setImageFile(context: Context, uri: Uri) {
        imageFile = ImageUtil.createImageFile(context, uri)
    }

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> get() = _uiState

    fun doRecord(isOnlySave: Boolean) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            recordUseCase.invoke(imageFile, recordType.value!!, recordWord.value!!)
                .onSuccess {
                    _uiState.value = UiState.Success(isOnlySave)
                }
                .onFailure {
                    _uiState.value = UiState.Error(it.message ?: "기록중 문제가 발생 했어요")
                }
        }
    }
}