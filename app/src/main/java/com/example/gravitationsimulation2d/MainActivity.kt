package com.example.gravitationsimulation2d

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.gravitationsimulation2d.data.Datasource
import com.example.gravitationsimulation2d.data.Planet
import com.example.gravitationsimulation2d.data.Simulation
import com.example.gravitationsimulation2d.func.*
import com.example.gravitationsimulation2d.model.SimulationRecord
import com.example.gravitationsimulation2d.ui.draw.*
import com.example.gravitationsimulation2d.ui.theme.GravitationSimulation2DTheme
import com.example.gravitationsimulation2d.viewmodel.HomeViewModel
import com.example.gravitationsimulation2d.viewmodel.HomeViewModelAbstract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/*

TODO LIST
when the screen is rotated, planets disappear
colors of the app bar and the status bar must be same
deltaTime varies significantly when a button is pressed, therefore the simulation acts differently whenever it runs
 */

enum class Screen {
    Init, Simulation, Records
}

enum class PopUp {
    Open, Close
}

val data_source: Datasource = Datasource()
var mp: MediaPlayer? = null
var audioWasPlaying = false

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel: HomeViewModel by viewModels()
        audioWasPlaying = false
        mp = MediaPlayer.create(this, R.raw.interstellar_main_theme)
        if (mp != null) {
            mp!!.isLooping = true
            mp!!.start()
        }
        setContent {
            GravitationSimulation2DTheme {
                GravitationSimulation2DApp(
                    homeViewModel = homeViewModel
                )
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
fun GravitationSimulation2DApp(
    homeViewModel: HomeViewModelAbstract
) {
    val recordListState = homeViewModel.simulationRecordFlow.collectAsState(initial = listOf())
    var currentScreen by rememberSaveable {
        mutableStateOf(Screen.Init)
    }
    val planets = remember {
        mutableListOf<Planet>().toMutableStateList()
    }
    var selectedPlanetChange by rememberSaveable {
        mutableStateOf(false)
    }
    val simulation by remember {
        mutableStateOf(Simulation(planets))
    }
    var popUpState by rememberSaveable {
        mutableStateOf(PopUp.Close)
    }

    if (currentScreen == Screen.Init) {
        Scaffold(
            topBar = {
                AppTopBarInitScreen(
                    {
                        if (mp != null) {                                       // audio button
                            if (mp!!.isPlaying) mp!!.pause() else mp!!.start()
                        }
                    }, {
                        if (!planets.isEmpty()) {
                            popUpState = PopUp.Open                             // save a simulation
                        }
                    }, {
                        currentScreen = Screen.Records                          // list records
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
                        val planet: Planet? = getSelectedInitialisedPlanet(planets)  // clicking a planet
                        data_source.updateSettingTextsPrevInputs(planet)
                        selectedPlanetChange = !selectedPlanetChange
                    }
                )
                PlanetImageCard()
                PlanetSettingTextList(selectedPlanetChange, Modifier.weight(1f))
                AppBottomBarInitScreen(
                    {
                        addPlanetToList(planets)                                // add planet button
                    }, {
                        deletePlanetFromList(planets)                             // delete planet button
                        data_source.nullAllPrevInputs()
                        selectedPlanetChange = !selectedPlanetChange
                    }, {
                        changeValuesOf(planets)                                 // change planet button
                        val planet: Planet? = getSelectedInitialisedPlanet(planets)
                        data_source.updateSettingTextsPrevInputs(planet)
                        selectedPlanetChange = !selectedPlanetChange
                    }, {
                        currentScreen = Screen.Simulation                       // run simulation button
                        unselectAllPlanets(planets)
                        data_source.setInitialPlanetStates(planets)
                        simulation.stop()
                    }
                )
                if (popUpState == PopUp.Open) {
                    RecordSavePopUp(
                        saveRecord = {title ->
                            if (title.isNotEmpty()) {
                                val planetList = convertPlanetsToPlanetListString(planets)     // save simulation
                                val record = SimulationRecord(title = title, planetList = planetList, date = getCurrentDate())
                                homeViewModel.addRecord(record)
                                popUpState = PopUp.Close
                            }
                        },
                        cancelSaving = {
                            popUpState = PopUp.Close
                        }
                    )
                }
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
    else if (currentScreen == Screen.Records) {
        Scaffold(
            topBar = {
                AppTopBarRecordsScreen(backToInitialisation = {
                    currentScreen = Screen.Init
                    }
                )
            }
        ) {
            SimulationRecordsList(
                recordListState.value,
                { record ->
                    homeViewModel.deleteRecord(record)
                },
                { record ->
                    val planetList = convertPlanetListStringToPlanets(record.planetList)
                    planets.removeAll { true }
                    for (planet in planetList) {
                        planets.add(planet)
                    }
                    currentScreen = Screen.Init
                }
            )
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    GravitationSimulation2DTheme(darkTheme = isSystemInDarkTheme()) {
        GravitationSimulation2DApp(
            homeViewModel = object : HomeViewModelAbstract {
                override val simulationRecordFlow: Flow<List<SimulationRecord>> get() = flowOf(listOf())
                override fun addRecord(record: SimulationRecord) {}
                override fun updateRecord(record: SimulationRecord) {}
                override fun deleteRecord(record: SimulationRecord) {}
            }
        )
    }
}


