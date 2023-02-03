package com.example.gravitationsimulation2d.data

import androidx.room.*
import com.example.gravitationsimulation2d.model.SimulationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SimulationRecordDao {
    @Query("select * from records")
    fun getAllFlow(): Flow<List<SimulationRecord>>

    @Insert
    suspend fun insert(record: SimulationRecord)

    @Delete
    suspend fun delete(record: SimulationRecord)

    @Update
    suspend fun update(record: SimulationRecord)
}