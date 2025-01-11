package com.ilgusu.data.service

import android.content.Context
import com.ilgusu.util.LoggerUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthService @Inject constructor(
    private val client: UserApiClient,
) {

    private fun Context.isKakaoTalkLoginAvailable(): Boolean
        = client.isKakaoTalkLoginAvailable(this)

    suspend fun signInWithKakao(context: Context): String {
        return suspendCoroutine { continuation ->
            val handleLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                try {
                    if (error != null) {
                        LoggerUtil.e("로그인 실패 ${error.message}", error)
                        continuation.resumeWithException(IllegalStateException("로그인 실패: ${error.message}"))
                    } else if (token == null) {
                        continuation.resumeWithException(IllegalStateException("토큰 발급 실패"))
                    } else {
                        val idToken = token.idToken ?: throw IllegalStateException("토큰이 빈 문자열")
                        continuation.resume(idToken)
                    }
                } catch (e: IllegalStateException) {
                    continuation.resumeWithException(e)
                }
            }

            if (context.isKakaoTalkLoginAvailable()) {
                client.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        LoggerUtil.e("카카오톡 로그인 실패: ${error.message}, 카카오 계정 로그인 시도", error)
                        client.loginWithKakaoAccount(context, callback = handleLoginCallback)
                    } else {
                        handleLoginCallback(token, null)
                    }
                }
            } else {
                client.loginWithKakaoAccount(context, callback = handleLoginCallback)
            }
        }
    }

    fun signOut(signOutListener: ((Throwable?) -> Unit)? = null) {
        client.logout { error ->
            if (error != null) {
                LoggerUtil.e("로그아웃 실패. SDK에서 토큰 삭제됨 ${error.message}", error)
            } else {
                LoggerUtil.i("로그아웃 성공. SDK에서 토큰 삭제됨")
            }
            signOutListener?.invoke(error)
        }
    }

    fun withdraw(withdrawListener: ((Throwable?) -> Unit)? = null) {
        client.unlink { error ->
            if (error != null) {
                LoggerUtil.e("회원탈퇴 실패 ${error.message}", error)
            } else {
                LoggerUtil.i("회원탈퇴 성공")
            }
            withdrawListener?.invoke(error)
        }
    }
}