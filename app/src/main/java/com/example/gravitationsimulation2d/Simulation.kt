package com.example.gravitationsimulation2d

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class SimulationState {
    STOPPED, RUNNING
}

class Simulation(var planets: MutableList<CelestialBody>) {
    private var prevTime: Long = 0L
    private var state by mutableStateOf(SimulationState.STOPPED)

    private fun applyForceToAll() {
        val size: Int = planets.size
        for (i in 0 until size-1) {
            for (j in i+1 until size) {
                planets[i].applyForce(planets[j])
                planets[j].applyForce(planets[i])
            }
        }
    }

    fun update(time: Long) {
        val delta: Long = time -prevTime
        val deltaT: Double = (delta/1E8) // use it for screen-independent results
        prevTime = time

        if (state == SimulationState.STOPPED) return
        applyForceToAll()
        for (planet in planets) {
            planet.move()
        }
        handleCollusion()
    }

    fun changeState() {
        state = if (state == SimulationState.RUNNING) SimulationState.STOPPED else SimulationState.RUNNING
    }

    fun stop() {
        state = SimulationState.STOPPED
    }

    private fun handleCollusion() {
        for (i in 0 until planets.size-1) {
            for (j in i+1 until planets.size) {
                val planet1 = planets[i]
                val planet2 = planets[j]
                if (planet1.overlapsWith(planet2)) {
                    if (planet1.mass > planet2.mass) {
                        planet1.collide(planet2)
                        planets.removeAt(j)
                    }
                    else {
                        planet2.collide(planet1)
                        planets.removeAt(i)
                    }
                }
            }
        }
    }
}