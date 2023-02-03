package com.example.gravitationsimulation2d.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gravitationsimulation2d.data.repository.SimulationRecordRepository
import com.example.gravitationsimulation2d.model.SimulationRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface HomeViewModelAbstract {
    val simulationRecordFlow: Flow<List<SimulationRecord>>
    fun addRecord(record: SimulationRecord)
    fun updateRecord(record: SimulationRecord)
    fun deleteRecord(record: SimulationRecord)
}

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val simulationRecordRepository: SimulationRecordRepository
): ViewModel(), HomeViewModelAbstract {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override val simulationRecordFlow: Flow<List<SimulationRecord>> = simulationRecordRepository.getAllFlow()

    override fun addRecord(record: SimulationRecord) {
        ioScope.launch {
            simulationRecordRepository.insert(record)
        }
    }
    override fun deleteRecord(record: SimulationRecord) {
        ioScope.launch {
            simulationRecordRepository.delete(record)
        }
    }
    override fun updateRecord(record: SimulationRecord) {
        ioScope.launch {
            simulationRecordRepository.update(record)
        }
    }
}