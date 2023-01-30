package com.example.gravitationsimulation2d

import androidx.annotation.DrawableRes

data class PlanetImage (
    @DrawableRes val imageId: Int,
    var selected: Boolean = false
)