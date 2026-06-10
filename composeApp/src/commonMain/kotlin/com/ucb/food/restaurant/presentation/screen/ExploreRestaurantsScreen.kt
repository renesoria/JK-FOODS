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
import com.example.designsystem.theme.AppTheme
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.viewmodel.HomeViewModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreRestaurantsScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(Res.string.explore_title), 
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    ) 
                },
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
                            drawPath(path, color = colors.textPrimary, style = Stroke(width = 2.dp.toPx()))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
        ) {
            // Buscador arriba
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onEvent(HomeEvent.OnSearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(stringResource(Res.string.explore_search_placeholder), color = colors.textSecondary) },
                leadingIcon = {
                    Box(modifier = Modifier.size(20.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(color = colors.textSecondary, radius = size.minDimension / 3, center = Offset(size.width * 0.4f, size.height * 0.4f), style = Stroke(width = 2f))
                            drawLine(color = colors.textSecondary, start = Offset(size.width * 0.6f, size.height * 0.6f), end = Offset(size.width * 0.9f, size.height * 0.9f), strokeWidth = 2f)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.textSecondary.copy(alpha = 0.5f),
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    cursorColor = colors.primary
                ),
                singleLine = true
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
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
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.textPrimary
        ),
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
                Text(text = restaurant.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AppTheme.colors.textPrimary)
                Text(text = restaurant.description, fontSize = 12.sp, color = AppTheme.colors.textSecondary, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "★", color = AppTheme.colors.primary, fontSize = 14.sp)
                    Text(text = " ${restaurant.overallRating}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.textPrimary)
                }
            }
            
            // Icono flecha para entrar
            val arrowColor = AppTheme.colors.textSecondary
            Canvas(modifier = Modifier.size(20.dp)) {
                val path = Path().apply {
                    moveTo(size.width * 0.3f, size.height * 0.3f)
                    lineTo(size.width * 0.7f, size.height * 0.5f)
                    lineTo(size.width * 0.3f, size.height * 0.7f)
                }
                drawPath(path, color = arrowColor, style = Stroke(width = 2.dp.toPx()))
            }
        }
    }
}
