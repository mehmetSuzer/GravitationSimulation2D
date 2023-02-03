package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gravitationsimulation2d.*
import com.example.gravitationsimulation2d.data.CelestialBody
import com.example.gravitationsimulation2d.func.unselectAllPlanets


@Composable
fun DrawInitialisedPlanetList(planets: MutableList<CelestialBody>, current_screen: Screen, planetBackGroundColor: Color, informUp: () -> Unit, modifier: Modifier = Modifier) {
    var state by rememberSaveable {
        mutableStateOf(false)
    }
    LazyRow(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .size(48.dp)
            .border(
                width = 4.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(planets) {celestial_body ->
            DrawInitialisedPlanetImage(
                celestial_body,
                planetBackGroundColor,
                state,
                {
                    state = !state
                    if (current_screen == Screen.Init) {
                        val prevSelected = celestial_body.selected
                        unselectAllPlanets(planets)
                        celestial_body.set_selected(!prevSelected)
                    } else if (current_screen == Screen.Simulation) {
                        celestial_body.set_selected(!celestial_body.selected)
                    }
                    informUp()
                }
            )
        }
    }
}

@Composable
fun DrawInitialisedPlanetImage(celestial_body: CelestialBody, backgroundColor: Color, state: Boolean, onChange: () -> Unit, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = celestial_body.imageId),
        contentDescription = null,
        modifier =
        if (celestial_body.selected) modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .background(backgroundColor)
            .clickable { onChange() }
        else modifier.clickable { onChange() }
    )
}
