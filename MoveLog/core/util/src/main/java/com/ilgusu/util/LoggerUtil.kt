package com.ilgusu.util

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object LoggerUtil {
    private const val LOGGER_TAG = "MOVELOG_LOGGER"
    private const val DEFAULT_METHOD_COUNT = 2
    private const val DEFAULT_METHOD_OFFSET = 7

    init {
        initializeLogger()
    }

    private fun initializeLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(DEFAULT_METHOD_COUNT)
            .methodOffset(DEFAULT_METHOD_OFFSET)
            .tag(LOGGER_TAG)
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    fun d(message: String, vararg args: Any?) {
        Logger.d(message, *args)
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Logger.e(throwable, message)
        } else {
            Logger.e(message)
        }
    }

    fun i(message: String, vararg args: Any?) {
        Logger.i(message, *args)
    }

    fun w(message: String, vararg args: Any?) {
        Logger.w(message, *args)
    }

    fun v(message: String, vararg args: Any?) {
        Logger.v(message, *args)
    }

    fun json(json: String?) {
        if (json.isNullOrEmpty()) {
            e("Empty/Null JSON")
            return
        }
        Logger.json(json)
    }

    fun xml(xml: String?) {
        if (xml.isNullOrEmpty()) {
            e("Empty/Null XML")
            return
        }
        Logger.xml(xml)
    }

    fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        Logger.log(priority, tag, message, throwable)
    }

    fun logMultiLine(message: String) {
        message.split("\n").forEach { line ->
            d(line)
        }
    }

    inline fun logExecutionTime(tag: String, block: () -> Unit) {
        val startTime = System.currentTimeMillis()
        block()
        val endTime = System.currentTimeMillis()
        d("$tag executed in ${endTime - startTime}ms")
    }
}