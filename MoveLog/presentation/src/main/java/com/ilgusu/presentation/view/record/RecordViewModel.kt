package com.ilgusu.presentation.view.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(

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
}