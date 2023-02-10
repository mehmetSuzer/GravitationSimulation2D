package com.example.gravitationsimulation2d.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "DEMO_KEY"

interface NasaApi {
    @GET("planetary/apod?api_key=$API_KEY")
    suspend fun getPictureOfTheDay(
        @Query("date") date: String
    ): NasaDto
}