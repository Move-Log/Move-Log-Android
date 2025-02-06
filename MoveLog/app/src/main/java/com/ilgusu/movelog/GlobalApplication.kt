package com.ilgusu.movelog

import android.app.Application
import com.ilgusu.util.LoggerUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        printStartingLog()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        clearCacheIfNeeded()
    }

    private fun printStartingLog() = LoggerUtil.v(this.getString(R.string.app_name) + " Start!")

    private fun clearCacheIfNeeded() {
        val cacheDir = this.cacheDir
        val cacheSize = getFolderSize(cacheDir)

        if (cacheSize > 50 * 1024 * 1024) {
            LoggerUtil.v("Cache Dir Clear!")
            cacheDir.deleteRecursively()
        }
    }

    private fun getFolderSize(directory: File): Long {
        var size = 0L
        directory.listFiles()?.forEach { file ->
            size += if (file.isDirectory) getFolderSize(file) else file.length()
        }
        return size
    }
}