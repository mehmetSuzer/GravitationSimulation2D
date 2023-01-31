package com.example.gravitationsimulation2d

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

const val G: Double = 6.674E-11                     // gravitational constant (N.m^2/kg^2)
const val SCALE: Double = 1.0E9                     // m/dp
var nextPlanetId: Long = 0

class CelestialBody(
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
    val radius: Int = 12
    val orbit = arrayOfNulls<OrbitDot>(ORBIT_SIZE)
    private var orbitIndex: Int = 0

    /*
    Returns true if the point is in the celestial body
     */
    fun containsPoint(x: Double, y: Double): Boolean {
        val xDiff = this.x-x
        val yDiff = this.y-y
        return (xDiff*xDiff + yDiff*yDiff).pow(0.5) < 2*this.radius
    }
    /*
    Returns distance in terms of m
     */
    private fun distance(other: CelestialBody): Double {
        val xDiff = this.x-other.x
        val yDiff = this.y-other.y
        return (xDiff*xDiff + yDiff*yDiff).pow(0.5) * SCALE
    }

    /*
    Updates the coordinates of the celestial body
     */
    fun move(deltaT: Double) {
        x += xVelocity * 1000.0 * deltaT / SCALE      // dp
        y += yVelocity * 1000.0 * deltaT / SCALE      // dp
        if (selected) {
            orbit[orbitIndex] = OrbitDot(x.toInt()+radius/2, y.toInt()+radius/2)
            orbitIndex = (orbitIndex+1) % ORBIT_SIZE
        }
    }

    /*
    Returns true if the distance between centers of two different celestial bodies is less than the sum of radius
     */
    fun overlapsWith(other: CelestialBody): Boolean {
        return this.id != other.id && distance(other)/SCALE < this.radius+other.radius
    }

    /*
    Applies force to another celestial body and changes its velocity
     */
    fun applyForce(other: CelestialBody, deltaT: Double) {
        val dist = this.distance(other)                             // m
        val force = G * this.mass * other.mass / (dist*dist)        // Newton
        val forceX = force * SCALE * (this.x - other.x) / dist      // Newton
        val forceY = force * SCALE * (this.y - other.y) / dist      // Newton
        other.xVelocity += forceX * deltaT / other.mass / 1000.0   // km/s
        other.yVelocity += forceY * deltaT / other.mass / 1000.0   // km/s
    }

    /*
    Returns a copy of the same celestial body
     */
    fun copy(): CelestialBody {
        return CelestialBody(x, y, xVelocity, yVelocity, massCoef, massPower, imageId, nextPlanetId++)
    }

    /*
    Collides with another lighter celestial body and consumes it
     */
    fun collide(other: CelestialBody) {
        this.xVelocity = (this.xVelocity*this.mass + other.xVelocity*other.mass) / this.mass
        this.yVelocity = (this.yVelocity*this.mass + other.yVelocity*other.mass) / this.mass
        this.mass += other.mass
        this.massPower = floor(log10(this.mass))
        this.massCoef = this.mass / 10.0.pow(this.massPower)
    }

    /*
    Sets the selected field to the given boolean
    If the celestial body is no longer selected, the orbit is cleared
     */
    fun set_selected(bool: Boolean) {
        selected = bool
        if (!selected) {
            orbit.fill(null)
            orbitIndex = 0
        }
    }
}

val CelestialBody.xOffset: Int get() = x.toInt()-radius/2
val CelestialBody.yOffset: Int get() = y.toInt()-radius/2

