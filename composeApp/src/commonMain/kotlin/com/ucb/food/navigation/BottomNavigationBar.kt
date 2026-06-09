package com.ucb.food.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

@Composable
fun BottomNavigationBar(
    currentRoute: NavRoute?,
    onNavigate: (NavRoute) -> Unit
) {
    val colors = AppTheme.colors
    // Definimos las rutas principales donde se debe mostrar la barra
    val mainRoutes = listOf(NavRoute.Home, NavRoute.MyReviews, NavRoute.Profile)
    
    // Si la ruta actual no es una de las principales, no mostramos la barra
    // (Por ejemplo, no queremos verla en el Login o en la pantalla de añadir review)
    if (currentRoute !in mainRoutes) return

    Surface(
        color = colors.background,
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = stringResource(Res.string.nav_home),
                isSelected = currentRoute is NavRoute.Home,
                onClick = { onNavigate(NavRoute.Home) },
                icon = { HomeIcon(it) }
            )
            BottomNavItem(
                label = stringResource(Res.string.nav_map),
                isSelected = false, 
                onClick = { /* TODO */ },
                icon = { MapIcon(it) }
            )
            BottomNavItem(
                label = stringResource(Res.string.nav_reviews),
                isSelected = currentRoute is NavRoute.MyReviews,
                onClick = { onNavigate(NavRoute.MyReviews) },
                icon = { StarIcon(it) }
            )
            BottomNavItem(
                label = stringResource(Res.string.nav_profile),
                isSelected = currentRoute is NavRoute.Profile,
                onClick = { onNavigate(NavRoute.Profile) },
                icon = { ProfileIcon(it) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit
) {
    val colors = AppTheme.colors
    val color = if (isSelected) colors.primary else colors.textPrimary.copy(alpha = 0.6f)
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon(color)
        Text(
            text = label, 
            color = color, 
            fontSize = 10.sp, 
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun HomeIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.1f, size.height * 0.9f)
            lineTo(size.width * 0.1f, size.height * 0.4f)
            lineTo(size.width * 0.5f, size.height * 0.1f)
            lineTo(size.width * 0.9f, size.height * 0.4f)
            lineTo(size.width * 0.9f, size.height * 0.9f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
fun MapIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        drawCircle(color = color, radius = size.minDimension / 3, style = Stroke(width = 2.dp.toPx()))
        drawCircle(color = color, radius = size.minDimension / 8, center = center)
    }
}

@Composable
fun StarIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.5f, size.height * 0.1f)
            lineTo(size.width * 0.63f, size.height * 0.38f)
            lineTo(size.width * 0.93f, size.height * 0.38f)
            lineTo(size.width * 0.69f, size.height * 0.56f)
            lineTo(size.width * 0.78f, size.height * 0.86f)
            lineTo(size.width * 0.5f, size.height * 0.68f)
            lineTo(size.width * 0.22f, size.height * 0.86f)
            lineTo(size.width * 0.31f, size.height * 0.56f)
            lineTo(size.width * 0.07f, size.height * 0.38f)
            lineTo(size.width * 0.37f, size.height * 0.38f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
fun ProfileIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        drawCircle(color = color, radius = size.minDimension / 4, center = Offset(size.width / 2, size.height * 0.35f), style = Stroke(width = 2.dp.toPx()))
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.85f)
            quadraticTo(size.width / 2, size.height * 0.6f, size.width * 0.8f, size.height * 0.85f)
        }
        drawPath(path, color = color, style = Stroke(width = 2.dp.toPx()))
    }
}
