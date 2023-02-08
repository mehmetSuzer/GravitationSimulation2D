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
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.pow

const val EarthYearConstant = 365.0*24.0*60.0
val speedLowerLimit = (1.0 - log(1000.0, EarthYearConstant)).toFloat()
val speedUpperLimit = (1.0 + log(1000.0, EarthYearConstant)).toFloat()

@Composable
fun SimulationSpeedPopUp(
    updateSpeed: (Double) -> Unit,
    cancelUpdating: () -> Unit,
    currentSpeed: Double,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = cancelUpdating) {
        val earthYearsPerSecondPower = rememberSaveable { mutableStateOf(log10(currentSpeed)/log10(EarthYearConstant)) }
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.background)
        ) {
            Text(
                text = stringResource(R.string.earth_years_per_simulation_seconds),
                style = MaterialTheme.typography.h3,
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
                    value = earthYearsPerSecondPower.value.toFloat(),
                    onValueChange = { newPower -> earthYearsPerSecondPower.value = newPower.toDouble() },
                    valueRange = speedLowerLimit..speedUpperLimit,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                )
                Text(
                    text = String.format("%.3f", EarthYearConstant.pow(earthYearsPerSecondPower.value-1.0)),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .width(56.dp)
                        .padding(end = 8.dp)
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
                    onClick = { updateSpeed(EarthYearConstant.pow(earthYearsPerSecondPower.value)) },
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