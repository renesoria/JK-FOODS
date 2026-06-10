package com.ucb.food.core.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.components.button.PrimaryButton
import com.example.designsystem.components.button.SecondaryButton
import com.example.designsystem.components.button.AppOutlinedButton
import com.example.designsystem.theme.AppTheme

@Composable
fun DesignSystemScreen(
    onNavigateBack: () -> Unit
) {
    val colors = AppTheme.colors
    
    Scaffold(
        containerColor = colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Design System Components",
                    style = AppTheme.typography.headlineSmall,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Typography Section
            Section(title = "Typography") {
                Text("Headline Large", style = AppTheme.typography.headlineLarge, color = colors.textPrimary)
                Text("Headline Medium", style = AppTheme.typography.headlineMedium, color = colors.textPrimary)
                Text("Headline Small", style = AppTheme.typography.headlineSmall, color = colors.textPrimary)
                Text("Body Large", style = AppTheme.typography.bodyLarge, color = colors.textPrimary)
                Text("Body Medium", style = AppTheme.typography.bodyMedium, color = colors.textPrimary)
                Text("Label Large", style = AppTheme.typography.labelLarge, color = colors.textPrimary)
            }

            // Buttons Section
            Section(title = "Buttons") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Primary Button", style = AppTheme.typography.bodyMedium, color = colors.textSecondary)
                    PrimaryButton(
                        text = "Primary Action",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Secondary Button", style = AppTheme.typography.bodyMedium, color = colors.textSecondary)
                    SecondaryButton(
                        text = "Secondary Action",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Outlined Button", style = AppTheme.typography.bodyMedium, color = colors.textSecondary)
                    AppOutlinedButton(
                        text = "Outlined Action",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Colors Section
            Section(title = "Colors") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorBox(color = colors.primary, label = "Primary")
                    ColorBox(color = colors.secondary, label = "Secondary")
                    ColorBox(color = colors.background, label = "Background", border = true)
                    ColorBox(color = colors.surface, label = "Surface", border = true)
                }
            }
            
            PrimaryButton(
                text = "Back to Home",
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = AppTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.primary
        )
        content()
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.textSecondary.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun ColorBox(color: Color, label: String, border: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color,
                    shape = RoundedCornerShape(8.dp)
                )
                .let { 
                    if (border) it.border(
                        1.dp, 
                        AppTheme.colors.textSecondary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) else it 
                }
        )
        Text(text = label, fontSize = 10.sp, color = AppTheme.colors.textSecondary)
    }
}
