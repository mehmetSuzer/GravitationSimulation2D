package com.example.gravitationsimulation2d.data.remote

import com.squareup.moshi.Json

data class NasaDto(
    @field:Json(name = "copyright")
    val copyright: String,
    @field:Json(name = "date")
    val date: String,
    @field:Json(name = "explanation")
    val explanation: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "url")
    val url: String
)
