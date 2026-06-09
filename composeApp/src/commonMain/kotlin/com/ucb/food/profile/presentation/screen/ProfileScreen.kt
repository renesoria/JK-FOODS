package com.ucb.food.profile.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.food.core.composable.ProfileAvatar
import com.ucb.food.profile.presentation.state.ProfileEffect
import com.ucb.food.profile.presentation.state.ProfileEvent
import com.ucb.food.profile.presentation.viewmodel.ProfileViewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.example.designsystem.theme.AppTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToMyReviews: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val colors = AppTheme.colors

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateBack -> onNavigateBack()
                ProfileEffect.NavigateToEditProfile -> onNavigateToEditProfile()
                ProfileEffect.NavigateToMyReviews -> onNavigateToMyReviews()
                ProfileEffect.NavigateToLogin -> onNavigateToLogin()
                is ProfileEffect.ShowError -> { /* Show error snackbar */ }
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileTopBar(onBackClick = { viewModel.onEvent(ProfileEvent.OnBackClick) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.surface)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Card
            UserHeaderCard(
                name = "${state.user?.firstName ?: ""} ${state.user?.lastName ?: ""}",
                email = state.user?.email ?: "",
                profilePicture = state.user?.profilePicture,
                onEditClick = { viewModel.onEvent(ProfileEvent.OnEditProfileClick) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats or info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.background)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.reviewCount.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Text(
                        stringResource(Res.string.profile_reviews_sent),
                        fontSize = 14.sp,
                        color = colors.textPrimary.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Icon for reviews
                    Box(modifier = Modifier.size(32.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val path = Path().apply {
                                moveTo(size.width * 0.2f, size.height * 0.1f)
                                lineTo(size.width * 0.8f, size.height * 0.1f)
                                lineTo(size.width * 0.8f, size.height * 0.9f)
                                lineTo(size.width * 0.2f, size.height * 0.9f)
                                close()
                            }
                            drawPath(path, color = Color(0xFFA67C00), style = Stroke(width = 1.dp.toPx()))
                            drawLine(Color(0xFFA67C00), start = Offset(size.width * 0.35f, size.height * 0.3f), end = Offset(size.width * 0.65f, size.height * 0.3f), strokeWidth = 1.dp.toPx())
                            drawLine(Color(0xFFA67C00), start = Offset(size.width * 0.35f, size.height * 0.5f), end = Offset(size.width * 0.65f, size.height * 0.5f), strokeWidth = 1.dp.toPx())
                            drawLine(Color(0xFFA67C00), start = Offset(size.width * 0.35f, size.height * 0.7f), end = Offset(size.width * 0.65f, size.height * 0.7f), strokeWidth = 1.dp.toPx())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Options
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.background)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF81C784)) },
                        title = stringResource(Res.string.profile_my_reviews),
                        subtitle = stringResource(Res.string.profile_my_reviews_subtitle),
                        onClick = { viewModel.onEvent(ProfileEvent.OnMyReviewsClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = colors.textPrimary.copy(alpha = 0.12f))
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF64B5F6)) },
                        title = stringResource(Res.string.profile_edit_profile),
                        subtitle = stringResource(Res.string.profile_edit_profile_subtitle),
                        onClick = { viewModel.onEvent(ProfileEvent.OnEditProfileClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = colors.textPrimary.copy(alpha = 0.12f))
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFFFFD54F)) },
                        title = stringResource(Res.string.profile_notifications),
                        subtitle = stringResource(Res.string.profile_notifications_subtitle),
                        onClick = { viewModel.onEvent(ProfileEvent.OnNotificationsClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = colors.textPrimary.copy(alpha = 0.12f))
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF4DB6AC)) },
                        title = stringResource(Res.string.profile_about),
                        subtitle = stringResource(Res.string.profile_about_subtitle),
                        onClick = { viewModel.onEvent(ProfileEvent.OnAboutClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = colors.textPrimary.copy(alpha = 0.12f))
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF424242)) },
                        title = "Modo Oscuro",
                        subtitle = "Cambia el tema de la aplicación",
                        onClick = { viewModel.onEvent(ProfileEvent.OnThemeToggle) },
                        trailing = {
                            Switch(
                                checked = state.isDarkMode,
                                onCheckedChange = { viewModel.onEvent(ProfileEvent.OnThemeToggle) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = colors.primary,
                                    checkedTrackColor = colors.primary.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = { viewModel.onEvent(ProfileEvent.OnLogoutClick) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.background)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logout Icon
                    Box(modifier = Modifier.size(24.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(color = Color(0xFFA67C00), style = Stroke(width = 2.dp.toPx()))
                            drawLine(Color(0xFFA67C00), start = Offset(size.width * 0.5f, size.height * 0.2f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = 2.dp.toPx())
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(stringResource(Res.string.profile_logout), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileTopBar(onBackClick: () -> Unit) {
    val colors = AppTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onBackClick() }
                        .padding(4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val path = Path().apply {
                            moveTo(size.width * 0.8f, size.height * 0.5f)
                            lineTo(size.width * 0.2f, size.height * 0.5f)
                            moveTo(size.width * 0.4f, size.height * 0.3f)
                            lineTo(size.width * 0.2f, size.height * 0.5f)
                            lineTo(size.width * 0.4f, size.height * 0.7f)
                        }
                        drawPath(
                            path = path,
                            color = colors.primary,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Column {
                    Text(
                        text = stringResource(Res.string.profile_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Text(
                        text = stringResource(Res.string.profile_subtitle),
                        fontSize = 12.sp,
                        color = colors.textPrimary.copy(alpha = 0.6f)
                    )
                }
            }
            // Notification bell icon
            Box(modifier = Modifier.size(32.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = Color(0xFFFFD54F), radius = size.minDimension / 3)
                    drawCircle(color = Color.Red, radius = size.minDimension / 8, center = Offset(size.width * 0.8f, size.height * 0.2f))
                }
            }
        }
    }
}

@Composable
fun UserHeaderCard(name: String, email: String, profilePicture: String?, onEditClick: () -> Unit) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.background)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(
                    base64Image = profilePicture,
                    size = 80.dp
                )
                
                // Edit icon on avatar
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(colors.primary)
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        drawLine(Color.White, start = Offset(0f, size.height), end = Offset(size.width, 0f), strokeWidth = 2f)
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                Text(text = email, fontSize = 14.sp, color = colors.textPrimary.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surface),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = colors.textPrimary.copy(alpha = 0.6f))
        }
        
        if (trailing != null) {
            trailing()
        } else {
            // Custom arrow icon
            Box(modifier = Modifier.size(24.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        moveTo(size.width * 0.3f, size.height * 0.3f)
                        lineTo(size.width * 0.7f, size.height * 0.5f)
                        lineTo(size.width * 0.3f, size.height * 0.7f)
                    }
                    drawPath(path, color = colors.textPrimary.copy(alpha = 0.3f), style = Stroke(width = 2.dp.toPx()))
                }
            }
        }
    }
}

@Composable
fun MenuIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawRect(color = color, size = size / 2f, topLeft = Offset(size.width * 0.25f, size.height * 0.25f))
    }
}
