package com.ucb.food.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await

actual suspend fun getRemoteConfigString(key: String): String {
    val remoteConfig = Firebase.remoteConfig
    // Force a fetch and activate to ensure we have the latest data without blocking the UI thread
    try {
        remoteConfig.fetchAndActivate().await()
    } catch (e: Exception) {
        // Fallback to current available config if fetch fails
    }
    return remoteConfig.getString(key)
}
