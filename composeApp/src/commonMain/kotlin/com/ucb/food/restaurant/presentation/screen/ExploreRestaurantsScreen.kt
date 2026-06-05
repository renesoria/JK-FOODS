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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.viewmodel.HomeViewModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreRestaurantsScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explorar Restaurantes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.7f, size.height * 0.5f)
                                lineTo(size.width * 0.3f, size.height * 0.5f)
                                moveTo(size.width * 0.5f, size.height * 0.3f)
                                lineTo(size.width * 0.3f, size.height * 0.5f)
                                lineTo(size.width * 0.5f, size.height * 0.7f)
                            }
                            drawPath(path, color = Color.Black, style = Stroke(width = 2.dp.toPx()))
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
                .background(Color(0xFFFAFAFA))
        ) {
            // Buscador arriba
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(HomeEvent.OnSearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar por nombre...") },
                leadingIcon = {
                    Box(modifier = Modifier.size(20.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(color = Color.Gray, radius = size.minDimension / 3, center = Offset(size.width * 0.4f, size.height * 0.4f), style = Stroke(width = 2f))
                            drawLine(color = Color.Gray, start = Offset(size.width * 0.6f, size.height * 0.6f), end = Offset(size.width * 0.9f, size.height * 0.9f), strokeWidth = 2f)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFA67C00)),
                singleLine = true
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFA67C00))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.filteredRestaurants) { restaurant ->
                        ExploreRestaurantItem(
                            restaurant = restaurant,
                            onClick = { onNavigateToDetail(restaurant.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExploreRestaurantItem(restaurant: RestaurantModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = restaurant.logoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = restaurant.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = restaurant.description, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "★", color = Color(0xFFA67C00), fontSize = 14.sp)
                    Text(text = " ${restaurant.overallRating}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            
            // Icono flecha para entrar
            Canvas(modifier = Modifier.size(20.dp)) {
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
