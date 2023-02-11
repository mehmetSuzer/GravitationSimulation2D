package com.example.gravitationsimulation2d.domain.repository

import com.example.gravitationsimulation2d.data.remote.NasaDto
import com.example.gravitationsimulation2d.domain.util.Resource

interface NasaRepository {
    suspend fun getNasaData(api_key: String, date: String): Resource<NasaDto>
}