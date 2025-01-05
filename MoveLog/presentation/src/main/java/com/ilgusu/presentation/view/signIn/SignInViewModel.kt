package com.ilgusu.presentation.view.signIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.model.AuthProvider
import com.ilgusu.domain.usecase.auth.GetTokenUseCase
import com.ilgusu.domain.usecase.auth.LoginUseCase
import com.ilgusu.domain.usecase.auth.SocialLoginUseCase
import com.ilgusu.presentation.util.UiState
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val loginUseCase: LoginUseCase,
    private val getTokenUseCase: GetTokenUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            val tokens = getTokenUseCase.invoke().first()
            LoggerUtil.i(tokens.toString())
            if(tokens.accessToken.isNotBlank()) {
                _uiState.value = UiState.Success(true)
            } else {
                _uiState.value = UiState.Success(false)
            }
        }
    }

    private val _uiState = MutableLiveData<UiState<Boolean>>()
    val uiState: LiveData<UiState<Boolean>> get() = _uiState

    private val _loginState = MutableLiveData<UiState<Boolean>>()
    val loginState: LiveData<UiState<Boolean>> get() = _loginState

    fun login(provider: AuthProvider) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                socialLoginUseCase.invoke(provider)
                    .onSuccess { idToken ->
                        LoggerUtil.d("${provider.name} 로그인 성공: $idToken")
                        serverLogin(idToken, provider)
                    }.onFailure { e ->
                        _loginState.value = UiState.Error(message = e.message ?: "소셜 로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(message = e.message ?: "소셜 로그인 중 예외 발생")
            }
        }
    }

    private fun serverLogin(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                loginUseCase.invoke(idToken, provider)
                    .onSuccess {
                        _loginState.value = UiState.Success(it)
                    }.onFailure { e ->
                        _loginState.value = UiState.Error(message = e.message ?: "로그인 실패")
                    }
            } catch (e: Exception) {
                _loginState.value = UiState.Error(message = e.message ?: "로그인 중 예외 발생")
            }
        }
    }
}