package com.example.gravitationsimulation2d.data.repository

import com.example.gravitationsimulation2d.data.SimulationRecordDao
import com.example.gravitationsimulation2d.model.SimulationRecord
import kotlinx.coroutines.flow.Flow

class SimulationRecordRepository(
    private val simulationRecord: SimulationRecordDao
) {
    fun getAllFlow(): Flow<List<SimulationRecord>> = simulationRecord.getAllFlow()
    suspend fun insert(record: SimulationRecord) = simulationRecord.insert(record)
    suspend fun update(record: SimulationRecord) = simulationRecord.update(record)
    suspend fun delete(record: SimulationRecord) = simulationRecord.delete(record)
}
