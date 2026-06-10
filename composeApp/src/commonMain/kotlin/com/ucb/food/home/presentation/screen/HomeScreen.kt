package com.ucb.food.home.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
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
import com.example.designsystem.components.divider.HorizontalDivider
import com.ucb.food.home.presentation.state.HomeEffect
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.viewmodel.HomeViewModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateToRestaurantDetail: (String) -> Unit = {},
    onNavigateToExplore: () -> Unit = {},
    onNavigateToDesignSystem: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onOpenDrawer: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppTheme.colors

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToProfile -> onNavigateToProfile()
                HomeEffect.NavigateToCart -> onNavigateToCart()
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
                is HomeEffect.NavigateToRestaurantDetail -> onNavigateToRestaurantDetail(effect.id)
                HomeEffect.OpenMenu -> onOpenDrawer()
                HomeEffect.NavigateToDesignSystem -> onNavigateToDesignSystem()
                HomeEffect.NavigateToMap -> onNavigateToMap()
            }
        }
    }

    Scaffold(
        topBar = { HomeTopBar(viewModel) },
        containerColor = colors.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp).zIndex(1f)) {
                    var isExpanded by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = {
                            viewModel.onEvent(HomeEvent.OnSearchQueryChanged(it))
                            isExpanded = it.isNotBlank()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        placeholder = { Text(stringResource(Res.string.home_search_placeholder), color = colors.textPrimary.copy(alpha = 0.6f)) },
                        leadingIcon = {
                            Box(modifier = Modifier.size(24.dp)) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(
                                        color = colors.primary,
                                        radius = size.minDimension / 3,
                                        center = Offset(size.width * 0.4f, size.height * 0.4f),
                                        style = Stroke(width = 2.dp.toPx())
                                    )
                                    drawLine(
                                        color = colors.primary,
                                        start = Offset(size.width * 0.6f, size.height * 0.6f),
                                        end = Offset(size.width * 0.9f, size.height * 0.9f),
                                        strokeWidth = 2.dp.toPx()
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.textPrimary.copy(alpha = 0.2f),
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary,
                            cursorColor = colors.primary,
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface
                        ),
                        singleLine = true
                    )

                    if (isExpanded && state.filteredRestaurants.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 250.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.surface)
                        ) {
                            LazyColumn(
                                modifier = Modifier.background(colors.surface)
                            ) {
                                items(state.filteredRestaurants) { restaurant ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                isExpanded = false
                                                viewModel.onEvent(HomeEvent.OnRestaurantClick(restaurant.id))   
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = restaurant.logoUrl,
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp).clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = restaurant.name, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text = "⭐ ${restaurant.overallRating}", fontSize = 12.sp, color = colors.primary)
                                    }
                                    HorizontalDivider(thickness = 0.5.dp)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Text(
                    text = stringResource(Res.string.home_hot_deals),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                if (state.hotDeals.isEmpty()) {
                    Image(
                        painter = painterResource(Res.drawable.paraHotDeals),
                        contentDescription = "Hot Deals",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.hotDeals) { deal ->
                            AsyncImage(
                                model = deal.imageUrl,
                                contentDescription = deal.title,
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.onEvent(HomeEvent.OnRestaurantClick(deal.restaurantId)) },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.home_explore_places),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Text(
                        text = stringResource(Res.string.home_view_all),
                        fontSize = 14.sp,
                        color = colors.primary,
                        modifier = Modifier.clickable { onNavigateToExplore() }
                    )
                }

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.restaurants) { restaurant -> 
                            RestaurantHorizontalCard(
                                restaurant = restaurant,
                                onClick = { viewModel.onEvent(HomeEvent.OnRestaurantClick(restaurant.id)) }     
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.home_top_ranking),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = stringResource(Res.string.home_community_favorites),
                    fontSize = 12.sp,
                    color = colors.textPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.topRatedRestaurants) { restaurant ->
                        RestaurantRankingCard(
                            restaurant = restaurant,
                            onClick = { viewModel.onEvent(HomeEvent.OnRestaurantClick(restaurant.id)) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(Res.drawable.fotoComida1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun RestaurantHorizontalCard(restaurant: RestaurantModel, onClick: () -> Unit) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = restaurant.logoUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurant.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                color = colors.textPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐", color = colors.primary, fontSize = 12.sp)
                Text(text = " ${restaurant.overallRating}", fontSize = 12.sp, color = colors.textPrimary.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun RestaurantRankingCard(restaurant: RestaurantModel, onClick: () -> Unit) {
    val colors = AppTheme.colors
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.background),
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
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = restaurant.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.textPrimary
                )
                Text(
                    text = stringResource(Res.string.home_score, restaurant.overallRating),
                    color = colors.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(viewModel: HomeViewModel) {
    val colors = AppTheme.colors
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
                drawLine(colors.primary, start = Offset(0f, size.height * 0.2f), end = Offset(size.width, size.height * 0.2f), strokeWidth = strokeWidth)
                drawLine(colors.primary, start = Offset(0f, size.height * 0.5f), end = Offset(size.width, size.height * 0.5f), strokeWidth = strokeWidth)
                drawLine(colors.primary, start = Offset(0f, size.height * 0.8f), end = Offset(size.width, size.height * 0.8f), strokeWidth = strokeWidth)
            }
        }

        Text(
            text = stringResource(Res.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary,
            modifier = Modifier.clickable { viewModel.onEvent(HomeEvent.OnLogoClick) }
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable { viewModel.onEvent(HomeEvent.OnProfileClick) }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = colors.primary, radius = size.minDimension / 2, style = Stroke(width = 2.dp.toPx()))
                drawCircle(color = colors.primary, radius = size.minDimension / 4, center = Offset(size.width / 2, size.height * 0.4f))
                val path = Path().apply {
                    moveTo(size.width * 0.25f, size.height * 0.85f)
                    quadraticTo(size.width / 2, size.height * 0.6f, size.width * 0.75f, size.height * 0.85f)    
                }
                drawPath(path, color = colors.primary, style = Stroke(width = 2.dp.toPx()))
            }
        }
    }
}
