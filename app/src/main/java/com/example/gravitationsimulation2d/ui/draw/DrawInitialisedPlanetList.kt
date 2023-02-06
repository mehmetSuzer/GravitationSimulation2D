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
import com.example.gravitationsimulation2d.data.Planet
import com.example.gravitationsimulation2d.func.unselectAllPlanets

val borderColor = Color(red = 105, green = 205, blue = 216)

@Composable
fun DrawInitialisedPlanetList(
    planets: MutableList<Planet>,
    current_screen: Screen,
    planetBackGroundColor: Color,
    informUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var state by rememberSaveable {
        mutableStateOf(false)
    }
    LazyRow(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
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
            DrawInitialisedPlanetImage(
                planet,
                planetBackGroundColor,
                state,
                {
                    state = !state
                    if (current_screen == Screen.Init) {
                        val prevSelected = planet.selected
                        unselectAllPlanets(planets)
                        planet.set_selected(!prevSelected)
                    } else if (current_screen == Screen.Simulation) {
                        planet.set_selected(!planet.selected)
                    }
                    informUp()
                }
            )
        }
    }
}

@Composable
fun DrawInitialisedPlanetImage(
    planet: Planet,
    backgroundColor: Color,
    state: Boolean,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = planet.imageId),
        contentDescription = null,
        modifier =
        if (planet.selected) modifier
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
