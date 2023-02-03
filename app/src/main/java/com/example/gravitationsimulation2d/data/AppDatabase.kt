package com.example.gravitationsimulation2d.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gravitationsimulation2d.model.SimulationRecord

@Database(entities = [SimulationRecord::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun simulationRecordDao(): SimulationRecordDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "records.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}