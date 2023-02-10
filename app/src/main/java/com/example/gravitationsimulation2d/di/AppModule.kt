package com.example.gravitationsimulation2d.di

import android.app.Application
import com.example.gravitationsimulation2d.data.AppDatabase
import com.example.gravitationsimulation2d.data.SimulationRecordDao
import com.example.gravitationsimulation2d.data.remote.NasaApi
import com.example.gravitationsimulation2d.data.repository.SimulationRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideNoteRepository(
        simulationRecordDao: SimulationRecordDao
    ): SimulationRecordRepository {
        return SimulationRecordRepository(simulationRecordDao)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getInstance(context = app)
    }

    @Singleton
    @Provides
    fun provideNoteDao(appDatabase: AppDatabase): SimulationRecordDao {
        return appDatabase.simulationRecordDao()
    }

    @Provides
    @Singleton
    fun provideNasaApi(): NasaApi {
        return Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}