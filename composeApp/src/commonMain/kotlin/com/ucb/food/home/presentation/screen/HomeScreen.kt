package com.ucb.food.home.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.ucb.food.home.presentation.state.HomeEffect
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.viewmodel.HomeViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.fotoComida1
import kotlinproject.composeapp.generated.resources.fotoComida2
import kotlinproject.composeapp.generated.resources.fotoComida3
import kotlinproject.composeapp.generated.resources.fotoComida4
import kotlinproject.composeapp.generated.resources.fotoComida5
import kotlinproject.composeapp.generated.resources.logoKindom
import kotlinproject.composeapp.generated.resources.paraHotDeals
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToProfile -> onNavigateToProfile()
                HomeEffect.NavigateToCart -> onNavigateToCart()
                HomeEffect.NavigateToLogin -> onNavigateToLogin()
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
            item {
                // Main Banner
                Image(
                    painter = painterResource(Res.drawable.fotoComida1),
                    contentDescription = "Main Food",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Explore Restaurants",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA67C00)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Grid Layout inside LazyColumn
                Row(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    Image(
                        painter = painterResource(Res.drawable.logoKindom),
                        contentDescription = "Kindom Logo",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(2f)) {
                        Row(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(Res.drawable.fotoComida2),
                                contentDescription = "Food 2",
                                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(Res.drawable.fotoComida3),
                                contentDescription = "Food 3",
                                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(Res.drawable.fotoComida4),
                                contentDescription = "Food 4",
                                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(Res.drawable.fotoComida5),
                                contentDescription = "Food 5",
                                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Hot Deals",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA67C00)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(Res.drawable.paraHotDeals),
                    contentDescription = "Hot Deals",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HomeTopBar(viewModel: HomeViewModel) {
    val scope = rememberCoroutineScope()
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
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA67C00)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        quadraticBezierTo(size.width / 2, size.height * 0.6f, size.width * 0.75f, size.height * 0.85f)
                    }
                    drawPath(path, color = Color(0xFFA67C00), style = Stroke(width = 2.dp.toPx()))
                }
            }
        }
    }
}
