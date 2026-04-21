package com.ucb.food.github.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.designsystem.components.button.PrimaryButton
import com.example.designsystem.components.input.BasicInput
import com.example.designsystem.theme.AppTheme
import com.ucb.food.github.presentation.state.GithubEvent
import com.ucb.food.github.presentation.viewmodel.GithubViewModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.github_find
import kotlinproject.composeapp.generated.resources.github_text_hint
import kotlinproject.composeapp.generated.resources.github_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GithubScreen(viewModel: GithubViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .background(AppTheme.colors.background) // Uso de tu token de color
            .padding(vertical = 40.dp, horizontal = 10.dp)
    ) {
        // --- AQUÍ EMPIEZA TU EJEMPLO DE USO ---
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.github_title),
                color = AppTheme.colors.primary, // Uso de tu color primario
                style = AppTheme.typography.headlineLarge, // Uso de tu tipografía
                fontWeight = FontWeight.Bold
            )
            
            BasicInput( // Tu componente personalizado
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = stringResource(Res.string.github_text_hint),
                value = state.nickname,
                onValueChange = {
                    viewModel.onEvent(GithubEvent.OnChangeAvatar(it))
                }
            )
            
            PrimaryButton( // Tu componente personalizado
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                onClick = {
                    viewModel.onEvent(GithubEvent.OnClickFind)
                },
                text = stringResource(Res.string.github_find)
            )
        }
        // --- FIN DEL EJEMPLO ---

        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(model = state.model.urlImage, contentDescription = null)
                Text(
                    state.model.avatar,
                    style = AppTheme.typography.labelLarge, // Cambiado a tu tipografía
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    state.model.company,
                    style = AppTheme.typography.bodyMedium, // Cambiado a tu tipografía
                    color = AppTheme.colors.textPrimary
                )
                Text(
                    state.model.bio,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textPrimary.copy(alpha = 0.6f)
                )
            }
        }
    }
}
