package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gravitationsimulation2d.R
import com.example.gravitationsimulation2d.Screen
import com.example.gravitationsimulation2d.data.Planet
import com.example.gravitationsimulation2d.mp


@Composable
fun AppTopBarInitScreen(
    handleAudio: () -> Unit,
    saveSimulation: () -> Unit,
    listRecords: () -> Unit,
    modifier: Modifier = Modifier
) {
    var audioIsPlaying by rememberSaveable {
        mutableStateOf(if (mp != null) mp!!.isPlaying else false)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { handleAudio(); audioIsPlaying = !audioIsPlaying }) {
            Icon(
                imageVector = if (audioIsPlaying) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.sound_on_off_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { saveSimulation() }) {
            Icon(
                imageVector = Icons.Filled.Save,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.save_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { listRecords() }) {
            Icon(
                imageVector = Icons.Filled.List,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.list_of_saved_records_button),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AppBottomBarInitScreen(
    addPlanet: () -> Unit,
    deletePlanet: () -> Unit,
    changePlanet: () -> Unit,
    runSimulation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { addPlanet() }) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.add_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { deletePlanet() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.delete_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { changePlanet() }) {
            Icon(
                imageVector = Icons.Filled.ChangeCircle,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.change_button),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { runSimulation() }) {
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.run_button),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AppTopBarSimulationScreen(
    planets: MutableList<Planet>,
    backToInitialisation: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onRestartClick: () -> Unit,
    current_screen: Screen,
    modifier: Modifier = Modifier
) {
    var playing by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { backToInitialisation() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.back_to_initialisation_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { onPlayPauseClick(); playing = !playing }) {
            Icon(
                imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.play_pause_button),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { onRestartClick(); playing = false }) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.restart_button),
                modifier = Modifier.size(24.dp)
            )
        }
        DrawInitialisedPlanetList(planets, current_screen, Color.Green, {  })
    }
}

@Composable
fun AppTopBarRecordsScreen(
    backToInitialisation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { backToInitialisation() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.back_to_initialisation_button),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.4f))
        Text(
            text = stringResource(R.string.records),
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}