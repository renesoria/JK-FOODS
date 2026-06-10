package com.ucb.food.core.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.designsystem.theme.AppTheme

@Composable
fun ProfileAvatar(
    base64Image: String?,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val colors = AppTheme.colors
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.surface),
        contentAlignment = Alignment.Center
    ) {
        if (!base64Image.isNullOrBlank()) {
            AsyncImage(
                model = base64Image,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Profile Avatar Placeholder
            Canvas(modifier = Modifier.size(size / 2)) {
                drawCircle(color = colors.primary, radius = this.size.minDimension / 2)
                drawCircle(color = colors.primary, radius = this.size.minDimension / 4, center = Offset(this.size.width / 2, this.size.height * 0.4f))
            }
        }
    }
}
