package com.example.gravitationsimulation2d.di

import android.app.Application
import com.example.gravitationsimulation2d.data.AppDatabase
import com.example.gravitationsimulation2d.data.SimulationRecordDao
import com.example.gravitationsimulation2d.data.repository.SimulationRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}