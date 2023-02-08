package com.example.gravitationsimulation2d.data

import com.example.gravitationsimulation2d.*

class Datasource {
    private var planetImages = listOf(
    PlanetImage(R.drawable.planet1),
    PlanetImage(R.drawable.planet2),
    PlanetImage(R.drawable.planet3),
    PlanetImage(R.drawable.planet4),
    PlanetImage(R.drawable.planet5),
    PlanetImage(R.drawable.planet6),
    PlanetImage(R.drawable.planet7),
    PlanetImage(R.drawable.planet8),
    PlanetImage(R.drawable.planet9),
    PlanetImage(R.drawable.planet10),
    PlanetImage(R.drawable.planet11)
    )

    private val xLowLimit = 10.0
    private val xHighLimit = 380.0
    private val yLowLimit = 10.0
    private val yHighLimit = 700.0
    private val xVelocityLowLimit = -100.0
    private val xVelocityHighLimit = 100.0
    private val yVelocityLowLimit = -100.0
    private val yVelocityHighLimit = 100.0
    private val massCoefLowLimit = 1.0
    private val massCoefHighLimit = 9.9999
    private val massPowerLowLimit = 5.0
    private val massPowerHighLimit = 36.0


    private var settingTexts = listOf(
        SettingText(R.string.x_settingText, R.string.dp_settingText ,"340.0", "$xLowLimit≤...≤$xHighLimit"),
        SettingText(R.string.y_settingText, R.string.dp_settingText, "200.0", "$yLowLimit≤...≤$yHighLimit"),
        SettingText(R.string.x_velocity_settingText, R.string.km_div_s_settingText, "0.0", "$xVelocityLowLimit≤...≤$xVelocityHighLimit"),
        SettingText(R.string.y_velocity_settingText, R.string.km_div_s_settingText, "30.0", "$yVelocityLowLimit≤...≤$yVelocityHighLimit"),
        SettingText(R.string.mass_coef_settingText, R.string.empty_string, "5.972","$massCoefLowLimit≤...≤$massCoefHighLimit"),
        SettingText(R.string.mass_power_settingText, R.string.kg_settingText, "24.0","$massPowerLowLimit≤...≤$massPowerHighLimit")
    )

    // Used to store the initial state of the planet initialised
    // Loaded when the user restarts the simulation
    private var initialPlanetStates = mutableListOf<Planet>()

    // Returns the list of user inputs
    fun getUserInputs(): List<String> {
        val result: MutableList<String> = mutableListOf()
        for (settingText in settingTexts) {
            result.add(settingText.userInput)
        }
        return result
    }

    // Returns a list of all planet images
    fun loadImages(): List<PlanetImage> {
        return planetImages
    }

    // Returns a list of all setting texts
    fun loadSettingTexts(): List<SettingText> {
        return settingTexts
    }

    // Select the given planet image and unselects all others
    // If it is already selected, the given image is unselected
    fun selectImage(selectedImage: PlanetImage) {
        for (image in planetImages) {
            val previousState = image.selected
            image.selected = image == selectedImage
            if (previousState)
                image.selected = false
        }
    }

    // Returns the selected image id
    // Returns -1 if no image is selected
    fun getSelectedImageId(): Int {
        for (image in planetImages) {
            if (image.selected)
                return image.imageId
        }
        return -1
    }

    // Check whether the user inputs are valid doubles or not
    // Then checks if the user inputs are in the valid interval
    // Returns true if all inputs are valid, otherwise returns false
    fun inputsAreValid(): Boolean {
        val userInputs = this.getUserInputs()
        val x: Double? = userInputs[0].toDoubleOrNull()
        val y: Double? = userInputs[1].toDoubleOrNull()
        val xVelocity: Double? = userInputs[2].toDoubleOrNull()
        val yVelocity: Double? = userInputs[3].toDoubleOrNull()
        val massCoef: Double? = userInputs[4].toDoubleOrNull()
        val massPower: Double? = userInputs[5].toDoubleOrNull()
        val imageId: Int = this.getSelectedImageId()

        if (x != null && y != null && xVelocity != null && yVelocity != null &&
            massCoef != null && massPower != null && imageId != -1 &&
            x in xLowLimit..xHighLimit &&
            y in yLowLimit..yHighLimit &&
            xVelocity in xVelocityLowLimit..xVelocityHighLimit &&
            yVelocity in yVelocityLowLimit..yVelocityHighLimit &&
            massCoef in massCoefLowLimit..massCoefHighLimit &&
            massPower in massPowerLowLimit..massPowerHighLimit
        ) {
           return true
        }
        return false
    }

    // Sets null to all previous inputs of setting texts
    fun nullAllPrevInputs() {
        settingTexts[0].prevInput = null
        settingTexts[1].prevInput = null
        settingTexts[2].prevInput = null
        settingTexts[3].prevInput = null
        settingTexts[4].prevInput = null
        settingTexts[5].prevInput = null
    }

    // If the planet is not null, sets all previous input
    // If it is null, clears previous inputs
    fun updateSettingTextsPrevInputs(planet: Planet?) {
        if (planet != null) {
            settingTexts[0].prevInput = "current value:${planet.x}"
            settingTexts[1].prevInput = "current value:${planet.y}"
            settingTexts[2].prevInput = "current value:${planet.xVelocity}"
            settingTexts[3].prevInput = "current value:${planet.yVelocity}"
            settingTexts[4].prevInput = "current value:${planet.massCoef}"
            settingTexts[5].prevInput = "current value:${planet.massPower}"
        }
        else {
            nullAllPrevInputs()
        }
    }

    // Stores the initial states of the planets
    fun setInitialPlanetStates(planets: MutableList<Planet>) {
        initialPlanetStates = mutableListOf()
        for (planet in planets) {
            initialPlanetStates.add(planet.copy())
        }
    }

    // Loads the initial state of the planets
    // This function is called when the user restarts the simulation
    fun reloadInitialPlanetStates(planets: MutableList<Planet>) {
        planets.removeAll { true }
        for (planet in initialPlanetStates) {
            planets.add(planet.copy())
        }
    }
}


