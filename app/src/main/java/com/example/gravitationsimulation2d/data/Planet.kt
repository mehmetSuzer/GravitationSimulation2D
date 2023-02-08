package com.example.gravitationsimulation2d.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

const val G = 6.674E-11                        // gravitational constant (N.m^2/kg^2)
var simulationScale: Double = 1.0E9            // m/dp
var simulationSpeed: Double = 365.0*24.0*60.0  //
var nextPlanetId: Long = 0

class Planet(
    xArg: Double,              // density pixel (dp)
    yArg: Double,              // density pixel (dp)
    xVelocityArg: Double,      // km/s (+ for right, - for left)
    yVelocityArg: Double,      // km/s (+ for down, - for up)
    massCoefArg: Double,
    massPowerArg: Double,
    @DrawableRes imageIdArg: Int,
    var id: Long
) {
    var x by mutableStateOf(xArg)                                      // dp
    var y by mutableStateOf(yArg)                                      // dp
    var xVelocity by mutableStateOf(xVelocityArg)                      // km/s
    var yVelocity by mutableStateOf(yVelocityArg)                      // km/s
    var massCoef by mutableStateOf(massCoefArg)
    var massPower by mutableStateOf(massPowerArg)
    var imageId by mutableStateOf(imageIdArg)
    var mass by mutableStateOf(massCoef * 10.0.pow(massPower))    // kg
    var selected: Boolean = false
    var consumed: Boolean = false
    val radius: Int = 12                                                // dp, used to handle collusions
    val orbit = arrayOfNulls<OrbitDot>(ORBIT_SIZE)
    private var orbitIndex: Int = 0
    private var prevX = x                                               // dp
    private var prevY = y                                               // dp

    // Returns the distance between the planet and the given point in terms of dp
    private fun distanceToPoint(x: Double, y: Double): Double {
        val xDiff = this.x-x
        val yDiff = this.y-y
        return (xDiff*xDiff + yDiff*yDiff).pow(0.5)
    }
    // Returns true if the point is in the planet
    fun containsPoint(x: Double, y: Double): Boolean {
        return distanceToPoint(x,y) < 2*this.radius
    }

    // Returns distance in terms of m
    private fun distance(other: Planet): Double {
        val xDiff = this.x-other.x
        val yDiff = this.y-other.y
        return (xDiff*xDiff + yDiff*yDiff).pow(0.5) * simulationScale
    }

    // Updates the coordinates of the planet and if it is selected, an orbit dot is added to "orbit"
    fun move() {
        x += xVelocity * 1000.0 * simulationSpeed / simulationScale      // dp
        y += yVelocity * 1000.0 * simulationSpeed / simulationScale      // dp
        if (selected) {
            if (distanceToPoint(prevX, prevY) > 10.0) {
                orbit[orbitIndex] = OrbitDot(x.toInt() + radius / 2, y.toInt() + radius / 2)
                orbitIndex = (orbitIndex + 1) % ORBIT_SIZE
                prevX = x
                prevY = y
            }
        }
    }

    // Returns true if the distance between centers of two different planets is less than the radius
    // Otherwise, returns false
    fun overlapsWith(other: Planet): Boolean {
        return this.id != other.id && distance(other)/simulationScale < this.radius
    }

    // Applies force to another planet and changes its velocity
    // F = G.m1.m2/R^2
    // F.dt = m.dv
    fun applyForce(other: Planet) {
        val dist = this.distance(other)                                               // m
        val force = G * this.mass * other.mass / (dist*dist)                          // Newton
        val forceX = force * simulationScale * (this.x - other.x) / dist              // Newton
        val forceY = force * simulationScale * (this.y - other.y) / dist              // Newton
        other.xVelocity += forceX * simulationSpeed / other.mass / 1000.0   // km/s
        other.yVelocity += forceY * simulationSpeed / other.mass / 1000.0   // km/s
    }

    // Returns a copy of the planet
    fun copy(): Planet {
        return Planet(x, y, xVelocity, yVelocity, massCoef, massPower, imageId, nextPlanetId++)
    }

    // Collides with another lighter planets and consumes it
    // new_m1 = m1+m2
    // new_v1 = (m1.old_v1 + m2.v2) / new_m1
    fun collide(other: Planet) {
        val totalMass = this.mass+other.mass
        this.xVelocity = (this.mass*this.xVelocity + other.mass*other.xVelocity) / totalMass
        this.yVelocity = (this.mass*this.yVelocity + other.mass*other.yVelocity) / totalMass
        this.mass = totalMass
        this.massPower = floor(log10(totalMass))
        this.massCoef = this.mass / 10.0.pow(totalMass)
    }

    // Sets the selected field to the given boolean
    // If the planet is no longer selected, the orbit is cleared
    fun set_selected(bool: Boolean) {
        selected = bool
        if (!selected) {
            orbit.fill(null)
            orbitIndex = 0
        }
    }
}

val Planet.xOffset: Int get() = x.toInt()-radius/2
val Planet.yOffset: Int get() = y.toInt()-radius/2

