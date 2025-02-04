package com.ilgusu.movelog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.auth.GetTokenUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UiState<Unit>>()
    val uiState: LiveData<UiState<Unit>> get() = _uiState

    fun checkLogin(){
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val result = getTokenUseCase.invoke().firstOrNull()
            if(result != null && result.accessToken.isNotBlank()) {
                _uiState.value = UiState.Success(Unit)
            } else {
                _uiState.value = UiState.Error("")
            }
        }
    }
 }