package com.ucb.food.restaurant.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ucb.food.core.utils.rememberImagePicker
import com.ucb.food.restaurant.presentation.state.AddReviewEffect
import com.ucb.food.restaurant.presentation.state.AddReviewEvent
import com.ucb.food.restaurant.presentation.viewmodel.AddReviewViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddReviewScreen(
    restaurantId: String,
    viewModel: AddReviewViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(restaurantId) {
        viewModel.onEvent(AddReviewEvent.LoadData(restaurantId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddReviewEffect.NavigateBack -> onNavigateBack()
                is AddReviewEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val imagePicker = rememberImagePicker { base64 ->
        viewModel.onEvent(AddReviewEvent.OnPhotoSelected(base64))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.onEvent(AddReviewEvent.OnCancelClick) }) {
                    Canvas(modifier = Modifier.size(24.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.7f, size.height * 0.5f)
                            lineTo(size.width * 0.3f, size.height * 0.5f)
                            moveTo(size.width * 0.5f, size.height * 0.3f)
                            lineTo(size.width * 0.3f, size.height * 0.5f)
                            lineTo(size.width * 0.5f, size.height * 0.7f)
                        }
                        drawPath(path, color = Color(0xFF8B6B11), style = Stroke(width = 2.dp.toPx()))
                    }
                }
                Text(
                    text = "Add Review",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B6B11),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto seleccionada o botón cámara
            item {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5))
                        .clickable { imagePicker() },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.photoBase64 != null) {
                        AsyncImage(
                            model = state.photoBase64,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Icono Cámara
                        Canvas(modifier = Modifier.size(48.dp)) {
                            drawRect(color = Color.Gray, style = Stroke(width = 2.dp.toPx()))
                            drawCircle(color = Color.Gray, radius = size.minDimension / 4)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Estrellas
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { index ->
                        Text(
                            text = "★",
                            fontSize = 40.sp,
                            color = if (index <= state.rating) Color(0xFFF0D680) else Color.LightGray,
                            modifier = Modifier.clickable { viewModel.onEvent(AddReviewEvent.OnRatingChanged(index)) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Checklist Attributes (Chips)
            item {
                Text(
                    text = "¿Qué tal el lugar?",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReviewChip("🚗 Parqueo", state.hasParking) { viewModel.onEvent(AddReviewEvent.OnToggleParking) }
                    ReviewChip("📶 Wi-Fi", state.hasWifi) { viewModel.onEvent(AddReviewEvent.OnToggleWifi) }
                    ReviewChip("💳 QR/Tarjeta", state.acceptsDigitalPayment) { viewModel.onEvent(AddReviewEvent.OnTogglePayment) }
                    ReviewChip("🐾 Pet Friendly", state.isPetFriendly) { viewModel.onEvent(AddReviewEvent.OnTogglePetFriendly) }
                    ReviewChip("🧒 Niños", state.hasKidsArea) { viewModel.onEvent(AddReviewEvent.OnToggleKidsArea) }
                    ReviewChip("⚡ Rápido", state.isFastService) { viewModel.onEvent(AddReviewEvent.OnToggleFastService) }
                    ReviewChip("🧼 Limpio", state.isClean) { viewModel.onEvent(AddReviewEvent.OnToggleClean) }
                    ReviewChip("🪑 Acogedor", state.isCozy) { viewModel.onEvent(AddReviewEvent.OnToggleCozy) }
                    ReviewChip("💰 Precio Justo", state.isFairPrice) { viewModel.onEvent(AddReviewEvent.OnToggleFairPrice) }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Comentario
            item {
                OutlinedTextField(
                    value = state.comment,
                    onValueChange = { viewModel.onEvent(AddReviewEvent.OnCommentChanged(it)) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Escribe tu reseña...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B6B11)
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Vincular platos
            item {
                Text(
                    text = "¿Qué consumiste?",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
            
            items(state.menu) { dish ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(AddReviewEvent.OnDishToggle(dish.id)) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.selectedDishes.contains(dish.id),
                        onCheckedChange = { viewModel.onEvent(AddReviewEvent.OnDishToggle(dish.id)) },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1B5E20))
                    )
                    Text(text = dish.name)
                }
            }

            // Botones de acción
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.onEvent(AddReviewEvent.OnSubmitClick) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Send", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                TextButton(
                    onClick = { viewModel.onEvent(AddReviewEvent.OnCancelClick) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = Color.Red, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ReviewChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF2E7D32), // Verde vibrante
            selectedLabelColor = Color.White,
            containerColor = Color(0xFFF5F5F5),
            labelColor = Color.Gray
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = Color.LightGray,
            selectedBorderColor = Color(0xFF1B5E20)
        ),
        shape = RoundedCornerShape(16.dp)
    )
}
