package com.ucb.food.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig

actual fun getRemoteConfigString(key: String): String {
    return Firebase.remoteConfig.getString(key)
}
