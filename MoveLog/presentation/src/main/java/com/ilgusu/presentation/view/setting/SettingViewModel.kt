package com.ilgusu.presentation.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilgusu.domain.usecase.auth.SocialWithdrawUseCase
import com.ilgusu.domain.usecase.auth.WithdrawUseCase
import com.ilgusu.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val socialWithdrawUseCase: SocialWithdrawUseCase,
    private val withdrawUseCase: WithdrawUseCase
) : ViewModel() {

    private val _withdrawState = MutableLiveData<UiState<Boolean>>()
    val withdrawState: LiveData<UiState<Boolean>> get() = _withdrawState

    fun withdraw() {
        _withdrawState.value = UiState.Loading

        viewModelScope.launch {
            socialWithdrawUseCase.invoke()
                .onFailure { _withdrawState.value = UiState.Error(it.message ?: "소셜 회원탈퇴 실패") }
                .onSuccess {
                    severWithdraw()
                }
        }
    }

    private fun severWithdraw() {
        viewModelScope.launch {
            withdrawUseCase.invoke()
                .onFailure { _withdrawState.value = UiState.Error(it.message ?: "서버 회원탈퇴 실패") }
                .onSuccess { _withdrawState.value = UiState.Success(true) }
        }
    }
}