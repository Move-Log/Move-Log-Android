package com.ilgusu.presentation.view.term

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.auth.SignUpUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> get() = _uiState

    fun signUp() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                signUpUseCase.invoke()
                    .onSuccess { idToken ->
                        LoggerUtil.d("회원가입 성공: $idToken")
                        _uiState.value = UiState.Success(idToken)
                    }.onFailure { e ->
                        _uiState.value = UiState.Error(message = e.message ?: "회원가입 실패")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(message = e.message ?: "회원가입 중 예외 발생")
            }
        }
    }
}