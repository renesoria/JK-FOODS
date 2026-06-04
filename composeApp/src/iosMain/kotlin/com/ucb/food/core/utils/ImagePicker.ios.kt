package com.ucb.food.core.utils

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit {
    return { /* iOS not implemented yet */ }
}
