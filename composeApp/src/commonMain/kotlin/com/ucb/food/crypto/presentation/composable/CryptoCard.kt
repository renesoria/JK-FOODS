package com.ucb.food.crypto.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ucb.food.crypto.domain.model.CryptoModel
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.components.divider.HorizontalDivider

@Composable
fun CryptoCard(crypto: CryptoModel) {
    val colors = AppTheme.colors

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = colors.background)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = crypto.image,
                    contentDescription = crypto.name,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = crypto.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textPrimary
                    )

                    Text(
                        text = crypto.symbol.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textPrimary.copy(alpha = 0.6f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${crypto.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textPrimary
                    )

                    val color = if (crypto.priceChange24h >= 0)
                        Color(0xFF4CAF50) else Color(0xFFF44336)

                    Text(
                        text = "${crypto.priceChange24h}%",
                        color = color,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(thickness = 1.dp) 

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rank #${crypto.marketCapRank}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = "H: $${crypto.high24h}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    color = colors.textPrimary
                )

                Text(
                    text = "L: $${crypto.low24h}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    color = colors.textPrimary
                )
            }
        }
    }
}
