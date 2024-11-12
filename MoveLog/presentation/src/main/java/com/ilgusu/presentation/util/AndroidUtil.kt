package com.ilgusu.presentation.util

import android.content.Context
import android.util.TypedValue
import android.view.View

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )
}

fun Context.pxToDp(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setOnSingleClickListener(debounceTime: Long = 300L, action: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            action()
        }
    }
}