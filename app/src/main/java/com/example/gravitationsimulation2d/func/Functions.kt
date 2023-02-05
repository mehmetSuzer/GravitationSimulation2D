package com.example.gravitationsimulation2d.func

import com.example.gravitationsimulation2d.data.CelestialBody
import com.example.gravitationsimulation2d.data.nextPlanetId
import com.example.gravitationsimulation2d.data_source
import java.util.Calendar
import kotlin.math.pow


fun addPlanetToList(planets: MutableList<CelestialBody>) {
    if (data_source.inputsAreValid()) {
        val userInputs = data_source.getUserInputs()
        val x: Double = userInputs[0].toDouble()
        val y: Double = userInputs[1].toDouble()
        val xVelocity: Double = userInputs[2].toDouble()
        val yVelocity: Double = userInputs[3].toDouble()
        val massCoef: Double = userInputs[4].toDouble()
        val massPower: Double = userInputs[5].toDouble()
        val imageId: Int = data_source.getSelectedImageId()
        if (!planets.any { planet -> planet.containsPoint(x,y) }) {
            planets.add(CelestialBody(x, y, xVelocity, yVelocity, massCoef, massPower, imageId, nextPlanetId++))
        }
    }
}

fun deletePlanetFromList(planets: MutableList<CelestialBody>) {
    planets.removeAll { it.selected }
}

fun unselectAllPlanets(planets: MutableList<CelestialBody>) {
    for (planet in planets) {
        planet.set_selected(false)
    }
}

fun getSelectedInitialisedPlanet(planets: MutableList<CelestialBody>): CelestialBody? {
    for (planet in planets) {
        if (planet.selected)
            return planet
    }
    return null
}

fun changeValuesOf(planets: MutableList<CelestialBody>) {
    val selectedPlanet: CelestialBody? = getSelectedInitialisedPlanet(planets)
    if (selectedPlanet != null && data_source.inputsAreValid()) {
        val userInputs = data_source.getUserInputs()
        val x = userInputs[0].toDouble()
        val y = userInputs[1].toDouble()
        if (!planets.any { planet -> planet.id != selectedPlanet.id && planet.containsPoint(x,y) }) {
            selectedPlanet.x = x
            selectedPlanet.y = y
            selectedPlanet.xVelocity = userInputs[2].toDouble()
            selectedPlanet.yVelocity = userInputs[3].toDouble()
            selectedPlanet.massCoef = userInputs[4].toDouble()
            selectedPlanet.massPower = userInputs[5].toDouble()
            selectedPlanet.mass = selectedPlanet.massCoef * 10.0.pow(selectedPlanet.massPower)
            selectedPlanet.imageId = data_source.getSelectedImageId()
        }
    }
}

fun convertPlanetsToPlanetListString(planets: MutableList<CelestialBody>): String {
    var result = ""
    for (index in 0 until planets.size) {
        val planet = planets[index]
        val planetStat = "${planet.x},${planet.y},${planet.xVelocity},${planet.yVelocity},${planet.massCoef},${planet.massPower},${planet.imageId}"
        result += if (index == planets.size-1) planetStat else "${planetStat};"
    }
    return result
}

fun convertPlanetListStringToPlanets(record: String): MutableList<CelestialBody> {
    val result = mutableListOf<CelestialBody>()
    val planetStats = record.split(';')
    for (planetStat in planetStats) {
        val values = planetStat.split(',')
        val x = values[0].toDouble()
        val y = values[1].toDouble()
        val xVelocity = values[2].toDouble()
        val yVelocity = values[3].toDouble()
        val massCoef = values[4].toDouble()
        val massPower = values[5].toDouble()
        val imageId = values[6].toInt()
        val planet = CelestialBody(x, y, xVelocity, yVelocity, massCoef, massPower, imageId, nextPlanetId++)
        result.add(planet)
    }
    return result
}

fun getCurrentDate():String { // "hh:mm:ss - dd/mm/yyyy"
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    val second = cal.get(Calendar.SECOND)
    return String.format("%02d:%02d:%02d - %02d/%02d/%04d", hour, minute, second, day, month, year)
}