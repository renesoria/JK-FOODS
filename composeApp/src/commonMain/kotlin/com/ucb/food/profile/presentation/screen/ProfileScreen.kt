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
import com.ucb.food.profile.presentation.state.ProfileEffect
import com.ucb.food.profile.presentation.state.ProfileEvent
import com.ucb.food.profile.presentation.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateBack -> onNavigateBack()
                ProfileEffect.NavigateToEditProfile -> onNavigateToEditProfile()
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
                .background(Color(0xFFF5F5F5))
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Card
            UserHeaderCard(
                name = "${state.user?.firstName ?: ""} ${state.user?.lastName ?: ""}",
                email = state.user?.email ?: "",
                onEditClick = { viewModel.onEvent(ProfileEvent.OnEditProfileClick) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats or info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("0", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("Reseñas enviadas", fontSize = 14.sp, color = Color.Gray)
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
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF81C784)) },
                        title = "Mis reseñas",
                        subtitle = "Revisa el estado de tus reseñas",
                        onClick = { viewModel.onEvent(ProfileEvent.OnMyReviewsClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF64B5F6)) },
                        title = "Editar perfil",
                        subtitle = "Actualiza tu información personal",
                        onClick = { viewModel.onEvent(ProfileEvent.OnEditProfileClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFFFFD54F)) },
                        title = "Notificaciones",
                        subtitle = "Configura tus preferencias",
                        onClick = { viewModel.onEvent(ProfileEvent.OnNotificationsClick) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                    ProfileMenuItem(
                        icon = { MenuIcon(Color(0xFF4DB6AC)) },
                        title = "Acerca de la app",
                        subtitle = "Versión 1.0.0",
                        onClick = { viewModel.onEvent(ProfileEvent.OnAboutClick) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = { viewModel.onEvent(ProfileEvent.OnLogoutClick) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
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
                    Text("Cerrar sesión", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileTopBar(onBackClick: () -> Unit) {
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
                            color = Color(0xFF2E7D32),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Column {
                    Text(
                        text = "Mi perfil",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Gestiona tu información y actividad",
                        fontSize = 12.sp,
                        color = Color.Gray
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
fun UserHeaderCard(name: String, email: String, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                // Profile Avatar Placeholder
                Canvas(modifier = Modifier.size(40.dp)) {
                    drawCircle(color = Color(0xFF2196F3), radius = size.minDimension / 2)
                    drawCircle(color = Color(0xFF2196F3), radius = size.minDimension / 4, center = Offset(size.width / 2, size.height * 0.4f))
                }
                
                // Edit icon on avatar
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32))
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
                Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = email, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
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
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        
        // Custom arrow icon
        Box(modifier = Modifier.size(24.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(size.width * 0.3f, size.height * 0.3f)
                    lineTo(size.width * 0.7f, size.height * 0.5f)
                    lineTo(size.width * 0.3f, size.height * 0.7f)
                }
                drawPath(path, color = Color.LightGray, style = Stroke(width = 2.dp.toPx()))
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
