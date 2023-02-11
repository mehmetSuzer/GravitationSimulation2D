package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gravitationsimulation2d.R

@Composable
fun DrawApiKeyPopUp(
    updateApiKey: (String) -> Unit,
    cancelUpdating: () -> Unit,
    currentApiKey: String,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = cancelUpdating) {
        val apiKeyState = rememberSaveable { mutableStateOf(currentApiKey) }
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.api_key),
                style = MaterialTheme.typography.button,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = apiKeyState.value,
                onValueChange = { newKey -> apiKeyState.value = newKey },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { cancelUpdating() },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel_button),
                        style = MaterialTheme.typography.button
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { updateApiKey(apiKeyState.value) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.save_button),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}