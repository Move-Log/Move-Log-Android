package com.ilgusu.movelog

import android.app.Application
import com.ilgusu.util.LoggerUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        printStartingLog()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }

    private fun printStartingLog() =
        LoggerUtil.v(this.getString(R.string.app_name) + " Start!")
}