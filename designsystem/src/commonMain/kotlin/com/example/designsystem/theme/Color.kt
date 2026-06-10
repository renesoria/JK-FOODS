package com.example.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val isLight: Boolean
)

val LightPalette = AppColors(
    primary = Color(0xFFFF9800),
    secondary = Color(0xFF757575),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    textPrimary = Color(0xFF000000),
    textSecondary = Color(0xFF757575),
    isLight = true
)

val DarkPalette = AppColors(
    primary = Color(0xFFFF9800),
    secondary = Color(0xFFBDBDBD),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFBDBDBD),
    isLight = false
)

val HighContrastPalette = AppColors(
    primary = Color(0xFFFFFF00),
    secondary = Color(0xFFFFFFFF),
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFFFFFFF),
    isLight = false
)
