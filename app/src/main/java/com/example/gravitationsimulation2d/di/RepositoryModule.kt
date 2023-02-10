package com.example.gravitationsimulation2d.di

import com.example.gravitationsimulation2d.data.repository.NasaRepositoryImpl
import com.example.gravitationsimulation2d.domain.repository.NasaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNasaRepository(
        nasaRepositoryImpl: NasaRepositoryImpl
    ): NasaRepository
}