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
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import com.example.designsystem.components.divider.HorizontalDivider
import com.example.designsystem.theme.AppTheme

@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    viewModel: RestaurantDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToAddReview: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val colors = AppTheme.colors

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
        containerColor = colors.background,
        floatingActionButton = {
            Button(
                onClick = { viewModel.onEvent(RestaurantDetailEvent.OnAddReviewClick) },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(stringResource(Res.string.restaurant_add_review), color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.primary)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val circleColor = colors.primary.copy(alpha = 0.1f)
                    drawCircle(
                        color = circleColor,
                        radius = 120.dp.toPx(),
                        center = center.copy(x = size.width * 0.9f, y = size.height * 0.3f)
                    )
                    drawCircle(
                        color = circleColor,
                        radius = 100.dp.toPx(),
                        center = center.copy(x = size.width * 0.1f, y = size.height * 0.6f)
                    )
                    drawCircle(
                        color = circleColor,
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
                                    drawPath(path, color = colors.primary, style = Stroke(width = 2.dp.toPx()))
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = state.restaurant?.name ?: stringResource(Res.string.restaurant_fallback_name),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primary
                            )
                        }
                    }

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

                    items(state.menu) { dish ->
                        DishListItem(dish)
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = stringResource(Res.string.restaurant_reviews_title),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                            textAlign = TextAlign.Start,
                            color = colors.textPrimary
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp), thickness = 1.dp)
                    }

                    if (state.reviews.isEmpty()) {
                        item {
                            Text(
                                stringResource(Res.string.restaurant_no_reviews),
                                color = colors.textSecondary,
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
    val colors = AppTheme.colors
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileAvatar(base64Image = review.userProfilePicture, size = 40.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = review.userName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = colors.textPrimary)
                    if (review.restaurantName.isNotBlank()) {
                        Text(text = stringResource(Res.string.restaurant_review_at, review.restaurantName), fontSize = 12.sp, color = colors.textSecondary)
                    }
                    Row {
                        (1..5).forEach { index ->
                            Text(
                                text = "★",
                                fontSize = 14.sp,
                                color = if (index <= review.rating) Color(0xFFF0D680) else colors.textSecondary.copy(alpha = 0.4f)      
                            )
                        }
                    }
                }
            }

            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = review.comment, fontSize = 14.sp, color = colors.textPrimary)
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

            FlowRow(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (review.hasParking) TinyTag(stringResource(Res.string.chip_parking))
                if (review.isPetFriendly) TinyTag(stringResource(Res.string.chip_pet_friendly))
                if (review.isFastService) TinyTag(stringResource(Res.string.chip_fast_service))
                if (review.isClean) TinyTag(stringResource(Res.string.chip_clean))
            }
        }
    }
}

@Composable
fun TinyTag(text: String) {
    val colors = AppTheme.colors
    Surface(
        color = colors.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            color = colors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DishListItem(dish: DishModel) {
    val colors = AppTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = dish.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dish.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Especialidad de la casa", 
                fontSize = 12.sp,
                color = colors.textSecondary,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${dish.price} Bs.",
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontSize = 14.sp
            )
        }
    }
}
