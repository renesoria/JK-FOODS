package com.ucb.food.signin.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.food.signin.presentation.state.SigninEffect
import com.ucb.food.signin.presentation.state.SigninEvent
import com.ucb.food.signin.presentation.viewmodel.SigninViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SigninScreen(
    viewModel: SigninViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SigninEffect.NavigateBack -> onNavigateBack()
                SigninEffect.NavigateToLogin -> onNavigateToLogin()
                SigninEffect.SignUpSuccess -> {
                    onNavigateToHome()
                }
                is SigninEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // Back Arrow manual
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.onEvent(SigninEvent.OnBackClick) }
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
                        color = Color(0xFFA67C00),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFA67C00)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // First Name and Last Name Row
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.firstName,
                onValueChange = { viewModel.onEvent(SigninEvent.OnFirstNameChanged(it)) },
                placeholder = { Text("First Name", fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFA67C00),
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = state.lastName,
                onValueChange = { viewModel.onEvent(SigninEvent.OnLastNameChanged(it)) },
                placeholder = { Text("Last Name", fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFA67C00),
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.mobileNumber,
            onValueChange = { viewModel.onEvent(SigninEvent.OnMobileNumberChanged(it)) },
            placeholder = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFA67C00),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(SigninEvent.OnEmailChanged(it)) },
            placeholder = { Text("E-Mail ID") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFA67C00),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Gender Dropdown Placeholder
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.gender,
                onValueChange = { },
                readOnly = true,
                placeholder = { Text("Male/Female") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val path = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width / 2, size.height)
                            close()
                        }
                        drawPath(path = path, color = Color.Gray)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFA67C00),
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = true }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Male") },
                    onClick = {
                        viewModel.onEvent(SigninEvent.OnGenderChanged("Male"))
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Female") },
                    onClick = {
                        viewModel.onEvent(SigninEvent.OnGenderChanged("Female"))
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(SigninEvent.OnPasswordChanged(it)) },
            placeholder = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFA67C00),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.address,
            onValueChange = { viewModel.onEvent(SigninEvent.OnAddressChanged(it)) },
            placeholder = { Text("ADDRESS") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFA67C00),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onEvent(SigninEvent.OnSignUpClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C5D00)
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("SignUp", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an Account? ", color = Color.Gray)
            Text(
                text = "Login",
                color = Color.Red,
                modifier = Modifier.clickable { viewModel.onEvent(SigninEvent.OnLoginClick) },
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
}