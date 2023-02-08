package com.example.gravitationsimulation2d.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class SimulationRecord(
    @PrimaryKey(autoGenerate = true) var roomId: Long? = null,
    val title: String,
    val planetList: String,  // planet1_stats;planet2_stats;...;planetN_stats
    val date: String,        // "hh:mm:ss-dd/mm/yyyy"
    val speed: Double,       // power of the simulation speed
    val scale: Double        // power of the simulation scale
)