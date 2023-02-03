package com.example.gravitationsimulation2d.data

import androidx.annotation.DrawableRes

data class PlanetImage (
    @DrawableRes val imageId: Int,
    var selected: Boolean = false
)