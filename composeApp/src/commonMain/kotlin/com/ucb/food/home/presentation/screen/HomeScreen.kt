package com.ucb.food.home.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import com.ucb.food.home.presentation.state.HomeEffect
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.viewmodel.HomeViewModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.fotoComida1
import kotlinproject.composeapp.generated.resources.paraHotDeals
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRestaurantDetail: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToProfile -> onNavigateToProfile()
                HomeEffect.NavigateToCart -> onNavigateToCart()
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                is HomeEffect.NavigateToRestaurantDetail -> onNavigateToRestaurantDetail(effect.id)
                HomeEffect.OpenMenu -> { /* Open drawer or menu */ }
            }
        }
    }

    Scaffold(
        topBar = { HomeTopBar(viewModel) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Buscador (Lupita)
            item {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(HomeEvent.OnSearchQueryChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Buscar restaurante...") },
                    leadingIcon = {
                        Box(modifier = Modifier.size(24.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = Color(0xFFA67C00),
                                    radius = size.minDimension / 3,
                                    center = Offset(size.width * 0.4f, size.height * 0.4f),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawLine(
                                    color = Color(0xFFA67C00),
                                    start = Offset(size.width * 0.6f, size.height * 0.6f),
                                    end = Offset(size.width * 0.9f, size.height * 0.9f),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFA67C00),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Main Banner
            item {
                Image(
                    painter = painterResource(Res.drawable.fotoComida1),
                    contentDescription = "Main Food",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Explore Restaurants",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA67C00)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Dynamic Restaurants Grid/List
            if (state.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFA67C00))
                    }
                }
            } else if (state.filteredRestaurants.isEmpty()) {
                item {
                    Text("No se encontraron restaurantes", color = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            } else {
                // We use a custom grid layout here since LazyColumn cannot host LazyVerticalGrid easily
                // For simplicity, we'll show them in rows of 2
                val chunks = state.filteredRestaurants.chunked(2)
                items(chunks) { rowItems ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        rowItems.forEach { restaurant ->
                            RestaurantCard(
                                restaurant = restaurant,
                                modifier = Modifier.weight(1f),
                                onClick = { viewModel.onEvent(HomeEvent.OnRestaurantClick(restaurant.id)) }
                            )
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Hot Deals",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA67C00)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(Res.drawable.paraHotDeals),
                    contentDescription = "Hot Deals",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: RestaurantModel, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = restaurant.logoUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurant.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "★", color = Color(0xFFA67C00), fontSize = 12.sp)
                Text(text = " ${restaurant.overallRating}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HomeTopBar(viewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { viewModel.onEvent(HomeEvent.OnMenuClick) }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.dp.toPx()
                drawLine(Color(0xFFA67C00), start = Offset(0f, size.height * 0.2f), end = Offset(size.width, size.height * 0.2f), strokeWidth = strokeWidth)
                drawLine(Color(0xFFA67C00), start = Offset(0f, size.height * 0.5f), end = Offset(size.width, size.height * 0.5f), strokeWidth = strokeWidth)
                drawLine(Color(0xFFA67C00), start = Offset(0f, size.height * 0.8f), end = Offset(size.width, size.height * 0.8f), strokeWidth = strokeWidth)
            }
        }

        Text(
            text = "RK Foods",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA67C00)
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable { viewModel.onEvent(HomeEvent.OnProfileClick) }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = Color(0xFFA67C00), radius = size.minDimension / 2, style = Stroke(width = 2.dp.toPx()))
                drawCircle(color = Color(0xFFA67C00), radius = size.minDimension / 4, center = Offset(size.width / 2, size.height * 0.4f))
                val path = Path().apply {
                    moveTo(size.width * 0.25f, size.height * 0.85f)
                    quadraticTo(size.width / 2, size.height * 0.6f, size.width * 0.75f, size.height * 0.85f)
                }
                drawPath(path, color = Color(0xFFA67C00), style = Stroke(width = 2.dp.toPx()))
            }
        }
    }
}
