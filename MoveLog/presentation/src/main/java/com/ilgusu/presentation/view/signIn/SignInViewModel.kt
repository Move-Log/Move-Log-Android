package com.ilgusu.presentation.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.usecase.auth.SocialLoginUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val socialLoginUseCase: SocialLoginUseCase
): ViewModel() {

    var isRegister = false

    private val _loginState = MutableLiveData<UiState<String>>()
    val loginState: LiveData<UiState<String>> get() = _loginState

    fun login(provider: AuthProvider) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                socialLoginUseCase.invoke(provider)
                    .onSuccess { idToken ->
                        LoggerUtil.d("로그인 성공: $idToken")
                        _loginState.value = UiState.Success(idToken)
                    }.onFailure { e ->
                        _loginState.value = UiState.Error(message = e.message ?: "소셜 로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(message = e.message ?: "소셜 로그인 중 예외 발생")
            }
        }
    }
}