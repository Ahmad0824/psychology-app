package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.network.GeminiClient
import com.example.ui.theme.CrisisContainer
import com.example.ui.theme.CrisisOnContainer
import com.example.ui.theme.CrisisRed

@Composable
fun CrisisSafetyCard(
    modifier: Modifier = Modifier,
    customMessage: String? = null,
    onDismiss: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("crisis_safety_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CrisisContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Emergency,
                    contentDescription = "Immediate Help Support",
                    tint = CrisisRed
                )
                Text(
                    text = "Crisis Support & Safety",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CrisisOnContainer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = customMessage ?: GeminiClient.SAFETY_SHUTDOWN_TEXT,
                style = MaterialTheme.typography.bodyMedium,
                color = CrisisOnContainer,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:988"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("call_988_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CrisisRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call 988", color = MaterialTheme.colorScheme.onPrimary)
                }

                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:741741")).apply {
                            putExtra("sms_body", "HOME")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("text_741741_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Sms,
                        contentDescription = null,
                        tint = CrisisRed
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Text 741741", color = CrisisRed)
                }
            }

            if (onDismiss != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Free, confidential 24/7 support available worldwide.",
                    style = MaterialTheme.typography.labelSmall,
                    color = CrisisOnContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}
