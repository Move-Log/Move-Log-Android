package com.ilgusu.navigation.extension

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination

/**
 * NavController의 백스택을 로그로 출력하는 확장 함수
 *
 * @param tag 로그 태그 (기본값: "NavBackstack")
 */
@SuppressLint("RestrictedApi")
fun NavController.printBackStack(tag: String = "NavBackstack") {
    val backStack = currentBackStack.value

    if (backStack.isEmpty()) {
        Log.d(tag, "BackStack is empty")
        return
    }

    val sb = StringBuilder()
    sb.appendLine("Current BackStack:")
    sb.appendLine("├── Size: ${backStack.size}")

    backStack.forEachIndexed { index, entry ->
        val destination = entry.destination
        val isLast = index == backStack.size - 1

        sb.append(if (isLast) "└── " else "├── ")
        sb.append("[$index] ")
        sb.append(formatDestination(destination))

        sb.appendLine()
    }

    Log.d(tag, sb.toString())
}

/**
 * NavDestination 정보를 포맷팅하는 private 함수
 */
private fun formatDestination(destination: NavDestination): String {
    return buildString {
        append("${destination.navigatorName}:")
        destination.route?.let { append(it) }
        destination.label?.let { append(it) }
        append(" (id: ${destination.id})")
    }
}

// 사용 예시:
// navController.printBackStack()
// navController.printBackStack("CustomTag")