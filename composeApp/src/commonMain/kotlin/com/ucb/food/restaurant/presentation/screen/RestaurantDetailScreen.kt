package com.ucb.food.restaurant.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ucb.food.core.composable.ProfileAvatar
import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.presentation.state.RestaurantDetailEffect
import com.ucb.food.restaurant.presentation.state.RestaurantDetailEvent
import com.ucb.food.restaurant.presentation.viewmodel.RestaurantDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    viewModel: RestaurantDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToAddReview: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(restaurantId) {
        viewModel.onEvent(RestaurantDetailEvent.LoadDetails(restaurantId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RestaurantDetailEffect.NavigateBack -> onNavigateBack()
                is RestaurantDetailEffect.NavigateToAddReview -> onNavigateToAddReview(effect.restaurantId)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(
                onClick = { viewModel.onEvent(RestaurantDetailEvent.OnAddReviewClick) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0D680)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Add Review", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFA67C00))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
                // Círculos amarillos decorativos de fondo
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color(0xFFFFF1A1).copy(alpha = 0.5f),
                        radius = 120.dp.toPx(),
                        center = center.copy(x = size.width * 0.9f, y = size.height * 0.3f)
                    )
                    drawCircle(
                        color = Color(0xFFFFF1A1).copy(alpha = 0.5f),
                        radius = 100.dp.toPx(),
                        center = center.copy(x = size.width * 0.1f, y = size.height * 0.6f)
                    )
                    drawCircle(
                        color = Color(0xFFFFF1A1).copy(alpha = 0.5f),
                        radius = 80.dp.toPx(),
                        center = center.copy(x = size.width * 0.8f, y = size.height * 0.95f)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Toolbar con botón atrás y Título
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { viewModel.onEvent(RestaurantDetailEvent.OnBackClick) }
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
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
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Text(
                                text = state.restaurant?.name ?: "Restaurant",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B6B11)
                            )
                        }
                    }

                    // Logo centrado (Cuadrado)
                    item {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            AsyncImage(
                                model = state.restaurant?.logoUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Lista de platos (Diseño Menu_List)
                    items(state.menu) { dish ->
                        DishListItem(dish)
                    }

                    // Sección de Reviews
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Opiniones de la gente",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                            textAlign = TextAlign.Start
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), thickness = 1.dp)
                    }

                    if (state.reviews.isEmpty()) {
                        item {
                            Text(
                                "Aún no hay reseñas. ¡Sé el primero!",
                                color = Color.Gray,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    } else {
                        items(state.reviews) { review ->
                            ReviewListItem(review)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun ReviewListItem(review: ReviewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileAvatar(base64Image = review.userProfilePicture, size = 40.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = review.userName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (review.restaurantName.isNotBlank()) {
                        Text(text = "en ${review.restaurantName}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Row {
                        (1..5).forEach { index ->
                            Text(
                                text = "★",
                                fontSize = 14.sp,
                                color = if (index <= review.rating) Color(0xFFF0D680) else Color.LightGray
                            )
                        }
                    }
                }
            }
            
            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = review.comment, fontSize = 14.sp, color = Color.DarkGray)
            }

            if (review.photoUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = review.photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Chips de atributos marcados (Solo los que son true)
            FlowRow(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (review.hasParking) TinyTag("🚗 Parqueo")
                if (review.isPetFriendly) TinyTag("🐾 Pet Friendly")
                if (review.isFastService) TinyTag("⚡ Rápido")
                if (review.isClean) TinyTag("🧼 Limpio")
            }
        }
    }
}

@Composable
fun TinyTag(text: String) {
    Surface(
        color = Color(0xFF2E7D32).copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DishListItem(dish: DishModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Imagen del plato a la izquierda
        AsyncImage(
            model = dish.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Texto del plato a la derecha
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dish.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "checken karachi special fried checken eith green spice", // Placeholder como en la imagen
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${dish.price} Bs.",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B6B11),
                fontSize = 14.sp
            )
        }
    }
}
