package com.ilgusu.presentation.view.term

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.usecase.auth.LoginUseCase
import com.ilgusu.domain.usecase.auth.SocialLoginUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Boolean>>()
    val loginState: LiveData<UiState<Boolean>> get() = _loginState

    fun login(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                loginUseCase.invoke(idToken, provider)
                    .onSuccess { idToken ->
                        LoggerUtil.d("로그인 성공: $idToken")
                        _loginState.value = UiState.Success(idToken)
                    }.onFailure { e ->
                        _loginState.value = UiState.Error(message = e.message ?: "로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(message = e.message ?: "로그인 중 예외 발생")
            }
        }
    }
}