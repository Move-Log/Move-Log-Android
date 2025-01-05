package com.ilgusu.data.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Process
import com.ilgusu.domain.repository.TokenRepository
import com.ilgusu.util.LoggerUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlin.system.exitProcess

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            runBlocking(Dispatchers.IO) {
                tokenRepository.clearTokens()
            }

            restartApplication(context)
        }

        return response
    }

    private fun restartApplication(mContext: Context) {
        val packageManager: PackageManager = mContext.packageManager
        val intent = packageManager.getLaunchIntentForPackage(mContext.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        mContext.startActivity(mainIntent)
        exitProcess(0)
    }
}