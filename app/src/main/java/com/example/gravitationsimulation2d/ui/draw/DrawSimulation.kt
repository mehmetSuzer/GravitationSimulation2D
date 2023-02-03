package com.example.gravitationsimulation2d.ui.draw


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gravitationsimulation2d.data.*

@Composable
fun DrawCelestialBody(planet: CelestialBody, modifier: Modifier = Modifier) {
    val planetSize = (planet.radius*2).dp
    if (planet.selected) {
        for (dot in planet.orbit) {
            if (dot != null) {
                Box(
                    modifier = Modifier
                        .offset(dot.x.dp, dot.y.dp)
                        .clip(CircleShape)
                        .size(ORBIT_DOT_SIZE.dp)
                        .background(MaterialTheme.colors.onBackground)
                )
            }
        }
    }
    Box(
        modifier = modifier
            .offset(planet.xOffset.dp, planet.yOffset.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = planet.imageId),
            modifier = Modifier.size(planetSize),
            contentDescription = null
        )
    }
}

@Composable
fun RunSimulation(simulation: Simulation, modifier: Modifier = Modifier) {
    val density = LocalDensity.current // use it to get results independent of screen aspects
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                simulation.update(it)
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clipToBounds()
    ) {
        simulation.planets.forEach {planet: CelestialBody ->
            DrawCelestialBody(planet)
        }
    }
}