package com.example.gravitationsimulation2d

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.gravitationsimulation2d.data.CelestialBody
import com.example.gravitationsimulation2d.data.Datasource
import com.example.gravitationsimulation2d.data.Simulation
import com.example.gravitationsimulation2d.func.*
import com.example.gravitationsimulation2d.ui.draw.*
import com.example.gravitationsimulation2d.ui.theme.GravitationSimulation2DTheme

/*
State in Jetpack Composable
https://developer.android.com/codelabs/jetpack-compose-state#8

TODO LIST
when the screen is rotated, planets disappear
colors of the app bar and the status bar must be same
enumeration of planets in the simulation screen
deltaTime varies significantly when a button is pressed, therefore the simulation acts differently whenever it runs

Remove unnecessary parts
 */

enum class Screen {
    Init, Simulation
}

val data_source: Datasource = Datasource()
val borderColor = Color(red = 105, green = 205, blue = 216)
var mp: MediaPlayer? = null
var audioWasPlaying = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioWasPlaying = false
        mp = MediaPlayer.create(this, R.raw.interstellar_main_theme)
        if (mp != null) {
            mp!!.isLooping = true
            mp!!.start()
        }
        setContent {
            GravitationSimulation2DTheme {
                GravitationSimulation2DApp()
            }
        }
    }
    override fun onPause() {        // User no longer interacts with the app, but the app is visible
        super.onPause()
        if (mp != null) {
            audioWasPlaying = mp!!.isPlaying
            mp!!.pause()
        }
    }
    override fun onResume() {       // The user starts interacting with the app
        super.onResume()
        if (mp != null && audioWasPlaying) {
            mp!!.start()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GravitationSimulation2DApp() {
    var currentScreen by rememberSaveable {
        mutableStateOf(Screen.Init)
    }
    val planets = remember {
        mutableListOf<CelestialBody>().toMutableStateList()
    }
    var selectedPlanetChange by rememberSaveable {
        mutableStateOf(false)
    }
    val simulation by remember {
        mutableStateOf(Simulation(planets))
    }

    if (currentScreen == Screen.Init) {
        Scaffold(
            topBar = {
                AppTopBarInitScreen(
                    {
                        addPlanetToList(planets)                          // add planet button
                    },
                    { deletePlanetFromList(planets)                       // delete planet button
                        data_source.nullAllPrevInputs()
                        selectedPlanetChange = !selectedPlanetChange
                    },
                    {
                        changeValuesOf(planets)                           // change planet button
                        val planet: CelestialBody? = getSelectedInitialisedPlanet(planets)
                        data_source.updateSettingTextsPrevInputs(planet)
                        selectedPlanetChange = !selectedPlanetChange
                    },
                    {
                        if (mp != null) {                                  // audio button
                            if (mp!!.isPlaying) mp!!.pause() else mp!!.start()
                        }
                    },
                    {
                        currentScreen = Screen.Simulation                 // run simulation button
                        unselectAllPlanets(planets)
                        data_source.setInitialPlanetStates(planets)
                        simulation.stop()
                    }
                )
            }
        ) {
            Column {
                DrawInitialisedPlanetList(
                    planets,
                    currentScreen,
                    Color.Red,
                    {
                        val planet: CelestialBody? = getSelectedInitialisedPlanet(planets)  // clicking a planet
                        data_source.updateSettingTextsPrevInputs(planet)
                        selectedPlanetChange = !selectedPlanetChange
                    }
                )
                PlanetImageCard()
                CelestialBodySettingTextList(selectedPlanetChange)
            }
        }
    }
    else if (currentScreen == Screen.Simulation) {
        Scaffold(
            topBar = {
                AppTopBarSimulationScreen(
                    planets,
                    { currentScreen = Screen.Init // back to the initialisation button
                        unselectAllPlanets(planets)
                        simulation.stop()
                        data_source.reloadInitialPlanetStates(planets)
                    },
                    { simulation.changeState() }, // play-pause button
                    { simulation.stop(); data_source.reloadInitialPlanetStates(planets) }, // restart button
                    currentScreen
                )
            }
        ) {
            RunSimulation(simulation)
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    GravitationSimulation2DTheme(darkTheme = isSystemInDarkTheme()) {
        GravitationSimulation2DApp()
    }
}


