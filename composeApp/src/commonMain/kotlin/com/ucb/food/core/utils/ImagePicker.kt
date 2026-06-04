package com.ucb.food.core.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(onImagePicked: (String) -> Unit): () -> Unit
