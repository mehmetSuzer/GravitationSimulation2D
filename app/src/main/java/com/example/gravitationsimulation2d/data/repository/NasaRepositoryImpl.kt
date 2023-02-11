package com.example.gravitationsimulation2d.data.repository

import com.example.gravitationsimulation2d.data.remote.NasaApi
import com.example.gravitationsimulation2d.data.remote.NasaDto
import com.example.gravitationsimulation2d.domain.repository.NasaRepository
import com.example.gravitationsimulation2d.domain.util.Resource
import javax.inject.Inject

class NasaRepositoryImpl @Inject constructor(
    private val api: NasaApi
): NasaRepository {
    override suspend fun getNasaData(api_key: String, date: String): Resource<NasaDto> {
        return try {
            Resource.Success(
                data = api.getPictureOfTheDay(api_key, date)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}