package com.example.gravitationsimulation2d.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("planetary/apod?")
    suspend fun getPictureOfTheDay(
        @Query("api_key") api_key: String,
        @Query("date") date: String
    ): NasaDto
}
