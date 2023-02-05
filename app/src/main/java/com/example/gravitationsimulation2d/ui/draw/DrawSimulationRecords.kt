package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gravitationsimulation2d.R
import com.example.gravitationsimulation2d.borderColor
import com.example.gravitationsimulation2d.data.PlanetImage
import com.example.gravitationsimulation2d.func.convertPlanetListStringToPlanets
import com.example.gravitationsimulation2d.model.SimulationRecord


@Composable
fun SimulationRecordsList(
    records: List<SimulationRecord>,
    deleteRecord: (SimulationRecord) -> Unit,
    loadSimulation: (SimulationRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.background(MaterialTheme.colors.background)
    ) {
        items(records) {record ->
            SimulationRecordUnit(record, { deleteRecord(record) }, { loadSimulation(record) })
        }
    }
}

@Composable
fun SimulationRecordUnit(
    record: SimulationRecord,
    deleteItself: () -> Unit,
    loadSimulation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val planets = convertPlanetListStringToPlanets(record.planetList)
    Card (
        modifier = modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = record.title,
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = record.date,
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                LazyRow(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .size(48.dp)
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(planets) {planet ->
                        DrawPlanetImage(planetImage = PlanetImage(planet.imageId))
                    }
                }
            }
            Column {
                IconButton(onClick = { loadSimulation() }) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = stringResource(R.string.load_simulation),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { deleteItself() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = stringResource(R.string.delete_button),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}