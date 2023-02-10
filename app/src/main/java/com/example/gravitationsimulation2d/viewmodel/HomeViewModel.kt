package com.example.gravitationsimulation2d.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gravitationsimulation2d.data.repository.SimulationRecordRepository
import com.example.gravitationsimulation2d.domain.repository.NasaRepository
import com.example.gravitationsimulation2d.domain.util.Resource
import com.example.gravitationsimulation2d.model.SimulationRecord
import com.example.gravitationsimulation2d.ui.draw.NasaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

interface HomeViewModelAbstract {
    val simulationRecordFlow: Flow<List<SimulationRecord>>
    var state: NasaState

    fun addRecord(record: SimulationRecord)
    fun updateRecord(record: SimulationRecord)
    fun deleteRecord(record: SimulationRecord)
    fun setCurrentDate()
    fun loadNasaDto(date: String)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val simulationRecordRepository: SimulationRecordRepository,
    private val nasaRepository: NasaRepository
): ViewModel(), HomeViewModelAbstract {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override val simulationRecordFlow: Flow<List<SimulationRecord>> = simulationRecordRepository.getAllFlow()
    override var state by mutableStateOf(NasaState())

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

    override fun setCurrentDate() {
        val cal = Calendar.getInstance()
        val date = String.format(
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        state = state.copy(
            date = date,
            nasaDto = null,
            isLoading = false,
            error = null
        )
    }

    override fun loadNasaDto(date: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = nasaRepository.getNasaData(date)) {
                is Resource.Success -> {
                    state = state.copy(
                        date = date,
                        nasaDto = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        date = date,
                        nasaDto = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}