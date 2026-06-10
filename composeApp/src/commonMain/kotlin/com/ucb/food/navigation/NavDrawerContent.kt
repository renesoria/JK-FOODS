package com.ucb.food.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import com.example.designsystem.theme.AppTheme
import com.example.designsystem.components.divider.HorizontalDivider

@Composable
fun NavDrawerContent(
    onNavigate: (NavRoute) -> Unit,
    onCloseDrawer: () -> Unit
) {
    val colors = AppTheme.colors
    ModalDrawerSheet(
        drawerContainerColor = colors.background,
        modifier = Modifier.width(300.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFFFFF1A1).copy(alpha = if (colors.isLight) 0.5f else 0.1f),
                    radius = 100.dp.toPx(),
                    center = center.copy(x = size.width * 0.8f, y = size.height * 0.3f)
                )
                drawCircle(
                    color = Color(0xFFFFF1A1).copy(alpha = if (colors.isLight) 0.5f else 0.1f),
                    radius = 80.dp.toPx(),
                    center = center.copy(x = size.width * 0.2f, y = size.height * 0.7f)
                )
                drawCircle(
                    color = Color(0xFFFFF1A1).copy(alpha = if (colors.isLight) 0.5f else 0.1f),
                    radius = 60.dp.toPx(),
                    center = center.copy(x = size.width * 0.7f, y = size.height * 0.9f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                IconButton(
                    onClick = onCloseDrawer,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Canvas(modifier = Modifier.size(24.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.2f, size.height * 0.2f)
                            lineTo(size.width * 0.8f, size.height * 0.8f)
                            moveTo(size.width * 0.8f, size.height * 0.2f)
                            lineTo(size.width * 0.2f, size.height * 0.8f)
                        }
                        drawPath(path, color = colors.primary, style = Stroke(width = 2.dp.toPx()))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                DrawerItem(stringResource(Res.string.nav_home)) { onNavigate(NavRoute.Home); onCloseDrawer() }  
                DrawerItem(stringResource(Res.string.nav_profile)) { onNavigate(NavRoute.Profile); onCloseDrawer() }
                DrawerItem(stringResource(Res.string.nav_reviews)) { onNavigate(NavRoute.MyReviews); onCloseDrawer() }
                DrawerItem(stringResource(Res.string.nav_notifications)) { /* TODO */ }
                DrawerItem(stringResource(Res.string.nav_search)) { onNavigate(NavRoute.Explore); onCloseDrawer() }
            }
        }
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    val colors = AppTheme.colors
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp)
        )
        HorizontalDivider(thickness = 1.dp)
    }
}
