package com.implementing.cozyspace.doodle_space.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raedapps.alwan.rememberAlwanState
import com.raedapps.alwan.ui.Alwan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerAlertBox(
    sentCurrentColor: (Color) -> Unit, onClickDialog: () -> Unit, currentColor: Color
) {

    AlertDialog(
        onDismissRequest = { onClickDialog() },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Color Picker",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Alwan(
                    onColorChanged = { sentCurrentColor(it) },
                    state = rememberAlwanState(initialColor = currentColor),
                    showAlphaSlider = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = { onClickDialog() },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
                    ) {
                        Text(text = "Done", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                    }
                }
            }
        }
    )
}