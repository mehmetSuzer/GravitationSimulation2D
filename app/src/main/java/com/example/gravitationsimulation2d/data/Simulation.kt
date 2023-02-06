package com.example.gravitationsimulation2d.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

private enum class SimulationState {
    STOPPED, RUNNING
}

class Simulation(var planets: MutableList<Planet>) {
    private var state by mutableStateOf(SimulationState.STOPPED)

    // All planets applies force to each other, assuming that the force does not change for deltaT seconds
    private fun applyForceToAll() {
        val size: Int = planets.size
        for (i in 0 until size-1) {
            for (j in i+1 until size) {
                planets[i].applyForce(planets[j])
                planets[j].applyForce(planets[i])
            }
        }
    }

    // Updates positions and velocities of planets in the simulation
    fun update() {
        if (state == SimulationState.STOPPED) return
        applyForceToAll()
        for (planet in planets) {
            planet.move()
        }
        handleCollusion()
    }

    // If the simulation is running, it stops, and vice versa
    fun changeState() {
        state = if (state == SimulationState.RUNNING) SimulationState.STOPPED else SimulationState.RUNNING
    }

    // Stops the simulation
    fun stop() {
        state = SimulationState.STOPPED
    }

    // If two planets get too close two each other, the bigger planet in terms of mass eats the other
    private fun handleCollusion() {
        for (i in 0 until planets.size-1) {
            val planet1 = planets[i]
            for (j in i+1 until planets.size) {
                val planet2 = planets[j]
                if (!planet1.consumed && !planet2.consumed && planet1.overlapsWith(planet2)) {
                    if (planet1.mass > planet2.mass) {
                        planet1.collide(planet2)
                        planet2.consumed = true
                    }
                    else {
                        planet2.collide(planet1)
                        planet1.consumed = true
                    }
                }
            }
        }
        planets.removeAll { planet -> planet.consumed }
    }
}

