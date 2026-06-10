package com.ucb.food.core.utils

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun bytesToBase64(bytes: ByteArray): String {
    return Base64.encode(bytes)
}

@OptIn(ExperimentalEncodingApi::class)
fun base64ToBytes(base64: String): ByteArray {
    return Base64.decode(base64)
}
