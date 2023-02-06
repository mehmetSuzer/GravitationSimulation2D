package com.example.gravitationsimulation2d.ui.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gravitationsimulation2d.data.PlanetImage
import com.example.gravitationsimulation2d.data_source


@Composable
fun PlanetImageCard(modifier: Modifier = Modifier) {
    Card (
        modifier = modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        var state by rememberSaveable {
            mutableStateOf(false)
        }
        LazyRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .size(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(data_source.loadImages()) { planetImage ->
                DrawPlanetImage(planetImage, state, { data_source.selectImage(planetImage); state = !state })
            }
        }
    }
}

@Composable
fun DrawPlanetImage(planetImage: PlanetImage, state: Boolean = false, onChange: () -> Unit = { }, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = planetImage.imageId),
        contentDescription = null,
        modifier =
        if (planetImage.selected) modifier
            .clickable { onChange() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
        else modifier.clickable { onChange() }
    )
}
