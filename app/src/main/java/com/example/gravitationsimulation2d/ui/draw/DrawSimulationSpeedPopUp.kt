package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
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
fun SimulationSpeedPopUp(
    updateSpeed: (Float) -> Unit,
    cancelUpdating: () -> Unit,
    currentSpeed: Float,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = cancelUpdating) {
        val speed = rememberSaveable { mutableStateOf(currentSpeed) }
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background)
        ) {
            Text(
                text = stringResource(id = R.string.simulation_speed),
                style = MaterialTheme.typography.button,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = speed.value,
                    onValueChange = { newSpeed -> speed.value = newSpeed },
                    valueRange = 0.1f..1f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                Text(
                    text = String.format("%.2f", speed.value.toDouble()),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.width(36.dp)
                )
            }
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
                    onClick = { updateSpeed(speed.value) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.update),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}