package com.ucb.food.firebase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

expect suspend fun getRemoteConfigString(key: String): String

suspend fun getRemoteConfigStringSafe(key: String): String = withContext(Dispatchers.Default) {
    try {
        getRemoteConfigString(key)
    } catch (e: Exception) {
        ""
    }
}
