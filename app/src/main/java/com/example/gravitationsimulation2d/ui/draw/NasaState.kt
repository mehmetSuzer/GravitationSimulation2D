package com.example.gravitationsimulation2d.ui.draw

import com.example.gravitationsimulation2d.data.remote.NasaDto

data class NasaState(
    val date: String = "",
    val nasaDto: NasaDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
