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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.food.core.composable.ProfileAvatar
import com.ucb.food.core.utils.rememberImagePicker
import com.ucb.food.profile.presentation.state.ProfileEditEvent
import com.ucb.food.profile.presentation.viewmodel.ProfileEditEffect
import com.ucb.food.profile.presentation.viewmodel.ProfileEditViewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Selector de imágenes nativo
    val imagePicker = rememberImagePicker { base64 ->
        viewModel.onEvent(ProfileEditEvent.OnProfilePictureChanged(base64))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEditEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.profile_edit_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { viewModel.onEvent(ProfileEditEvent.OnBackClick) }
                            .padding(12.dp)
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
                                color = Color.Black,
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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
            // Avatar con icono de lapicito
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { 
                        imagePicker() // Abrir galería
                    },
                contentAlignment = Alignment.Center
            ) {
                ProfileAvatar(
                    base64Image = state.profilePicture,
                    size = 100.dp
                )
                
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF1B5E20)),
                    contentAlignment = Alignment.Center
                ) {
                    // Dibujo de un Lapicito (Pencil) con Canvas
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val strokeWidth = 1.5.dp.toPx()
                        
                        // Cuerpo del lápiz
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(size.width * 0.3f, size.height * 0.1f),
                            size = Size(size.width * 0.3f, size.height * 0.6f),
                            style = Stroke(width = strokeWidth)
                        )
                        
                        // Punta (triángulo)
                        val path = Path().apply {
                            moveTo(size.width * 0.3f, size.height * 0.7f)
                            lineTo(size.width * 0.6f, size.height * 0.7f)
                            lineTo(size.width * 0.45f, size.height * 0.95f)
                            close()
                        }
                        drawPath(path, color = Color.White)
                        
                        // Goma (arriba)
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(size.width * 0.3f, size.height *(0.1f)),
                            size = Size(size.width * 0.3f, size.height * 0.15f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditField(label = stringResource(Res.string.profile_edit_first_name), value = state.firstName, onValueChange = { viewModel.onEvent(ProfileEditEvent.OnFirstNameChanged(it)) })
                    EditField(label = stringResource(Res.string.profile_edit_last_name), value = state.lastName, onValueChange = { viewModel.onEvent(ProfileEditEvent.OnLastNameChanged(it)) })
                    EditField(label = stringResource(Res.string.profile_edit_email), value = state.email, onValueChange = {}, readOnly = true)
                    EditField(label = stringResource(Res.string.profile_edit_address), value = state.address, onValueChange = { viewModel.onEvent(ProfileEditEvent.OnAddressChanged(it)) })
                    EditField(
                        label = stringResource(Res.string.profile_edit_password),
                        value = state.password,
                        onValueChange = { viewModel.onEvent(ProfileEditEvent.OnPasswordChanged(it)) },
                        isPassword = true,
                        isPasswordVisible = state.isPasswordVisible,
                        onToggleVisibility = { viewModel.onEvent(ProfileEditEvent.OnTogglePasswordVisibility) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onEvent(ProfileEditEvent.OnSaveClick) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(stringResource(Res.string.profile_edit_save), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            if (state.error != null) {
                Text(text = state.error!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onToggleVisibility) {
                        Box(modifier = Modifier.size(24.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Draw an "Eye" shape
                                drawCircle(
                                    color = if (isPasswordVisible) Color(0xFF1B5E20) else Color.Gray,
                                    radius = size.minDimension / 4,
                                    center = Offset(size.width / 2, size.height / 2)
                                )
                                drawPath(
                                    path = Path().apply {
                                        moveTo(0f, size.height / 2)
                                        quadraticTo(size.width / 2, 0f, size.width, size.height / 2)
                                        quadraticTo(size.width / 2, size.height, 0f, size.height / 2)
                                    },
                                    color = if (isPasswordVisible) Color(0xFF1B5E20) else Color.Gray,
                                    style = Stroke(width = 2f)
                                )
                                if (!isPasswordVisible) {
                                    drawLine(
                                        color = Color.Gray,
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = 2f
                                    )
                                }
                            }
                        }
                    }
                }
            } else null,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.LightGray,
                focusedIndicatorColor = Color(0xFF1B5E20)
            )
        )
    }
}
