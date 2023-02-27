package com.example.gravitationsimulation2d

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.gravitationsimulation2d.data.*
import com.example.gravitationsimulation2d.func.*
import com.example.gravitationsimulation2d.model.SimulationRecord
import com.example.gravitationsimulation2d.ui.draw.*
import com.example.gravitationsimulation2d.ui.theme.GravitationSimulation2DTheme
import com.example.gravitationsimulation2d.viewmodel.HomeViewModel
import com.example.gravitationsimulation2d.viewmodel.HomeViewModelAbstract
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import kotlin.math.log10
import kotlin.math.pow

/*
TODO LIST
colors of the app bar and the status bar must be same
 */

enum class Screen {
    Init, Simulation, Records, AstronomyPicture
}

enum class PopUp {
    Save, Speed, Scale, Close, ApiKey
}

const val API_KEY_FILE_NAME = "api_key.txt"

val recordsRef = FirebaseDatabase.getInstance().getReference("records")
val data_source: Datasource = Datasource()
var mp: MediaPlayer? = null
var audioWasPlaying = false
var apiKey = "DEMO_KEY"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewModel.setCurrentDate()

        audioWasPlaying = false
        mp = MediaPlayer.create(this, R.raw.interstellar_main_theme)
        if (mp != null) {
            mp!!.isLooping = true
            mp!!.start()
        }

        if (API_KEY_FILE_NAME in this.fileList()) {
            apiKey = this.openFileInput(API_KEY_FILE_NAME).bufferedReader().readText()
        }

        setContent {
            GravitationSimulation2DTheme {
                GravitationSimulation2DApp(
                    viewModel = viewModel,
                    context = this
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
fun GravitationSimulation2DApp(viewModel: HomeViewModelAbstract, context: Context) {
    val recordListState = viewModel.simulationRecordFlow.collectAsState(initial = listOf())
    var currentScreen by rememberSaveable { mutableStateOf(Screen.Init) }
    val planets = remember { mutableListOf<Planet>().toMutableStateList() }
    var selectedPlanetChange by rememberSaveable { mutableStateOf(false) }
    val simulation by remember { mutableStateOf(Simulation(planets)) }
    var popUpState by rememberSaveable { mutableStateOf(PopUp.Close) }

    if (currentScreen == Screen.Init) {
        Scaffold(
            topBar = {
                AppTopBarInitScreen(
                    { currentScreen = Screen.AstronomyPicture },                // go to "Astronomy Picture of the Day" Page
                    { popUpState = PopUp.Scale },                               // change scale of the simulation
                    { popUpState = PopUp.Speed },                               // change speed of the simulation
                    {
                        if (mp != null) {                                       // turn on-off audio
                            if (mp!!.isPlaying) mp!!.pause() else mp!!.start()
                        }
                    },
                    {
                        if (!planets.isEmpty()) {
                            popUpState = PopUp.Save                             // save a simulation
                        }
                    },
                    { currentScreen = Screen.Records }                           // list records
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
                if (popUpState == PopUp.Save) {
                    RecordSavePopUp(
                        saveRecord = {title ->
                            if (title.isNotEmpty()) {
                                val planetList = convertPlanetsToPlanetListString(planets)     // save simulation
                                val record = SimulationRecord(
                                    title = title,
                                    planetList = planetList,
                                    date = getCurrentDate(),
                                    speed = log10(simulationSpeed),
                                    scale = log10(simulationScale)
                                )
                                viewModel.addRecord(record)
                                recordsRef.child(firebaseId(record)).setValue(record)
                                popUpState = PopUp.Close
                            }
                        },
                        cancelSaving = {
                            popUpState = PopUp.Close
                        }
                    )
                }
                else if (popUpState == PopUp.Speed) {
                    SimulationSpeedPopUp(
                        updateSpeed = { newSpeed -> simulationSpeed = newSpeed; popUpState = PopUp.Close },
                        cancelUpdating = { popUpState = PopUp.Close },
                        currentSpeed = simulationSpeed
                    )
                }
                else if (popUpState == PopUp.Scale) {
                    SimulationScalePopUp(
                        updateScale = { newScale -> simulationScale = newScale; popUpState = PopUp.Close },
                        cancelUpdating = { popUpState = PopUp.Close },
                        currentScale = simulationScale
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
                AppTopBarRecordsScreen(backToInitialisation = { currentScreen = Screen.Init })
            }
        ) {
            SimulationRecordsList(
                recordListState.value,
                { record ->
                    recordsRef.child(firebaseId(record)).removeValue()                      // delete record
                    viewModel.deleteRecord(record)
                },
                { record ->
                    val planetList = convertPlanetListStringToPlanets(record.planetList)    // load the simulation
                    planets.removeAll { true }
                    for (planet in planetList) {
                        planets.add(planet)
                    }
                    simulationSpeed = 10.0.pow(record.speed)
                    simulationScale = 10.0.pow(record.scale)
                    currentScreen = Screen.Init
                }
            )
        }
    }
    else if (currentScreen == Screen.AstronomyPicture) {
        Scaffold(
            topBar = {
                AppTopBarAstronomyPicture(
                    date = viewModel.state.date,
                    { popUpState = PopUp.ApiKey },              // set the api key
                    goBack = { currentScreen = Screen.Init },   // go back to init screen
                    onSearchClick = { date ->
                        val stripedDate = validDate(date)       // search the astronomy picture of the day
                        if (stripedDate != null) {
                            viewModel.loadNasaDto(apiKey, stripedDate)
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                DrawNasaCard(
                    nasaDto = viewModel.state.nasaDto,
                    modifier = Modifier.fillMaxSize()
                )
                if (viewModel.state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                viewModel.state.error?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            if (popUpState == PopUp.ApiKey) {
                DrawApiKeyPopUp(
                    updateApiKey = { newApiKey ->
                        if (API_KEY_FILE_NAME !in context.fileList()) {
                            File(context.filesDir, API_KEY_FILE_NAME)
                        }
                        context.openFileOutput(API_KEY_FILE_NAME, Context.MODE_PRIVATE).use {
                            it.write(newApiKey.toByteArray())
                        }
                        apiKey = context.openFileInput(API_KEY_FILE_NAME).bufferedReader().readText()
                        popUpState = PopUp.Close },
                    cancelUpdating = { popUpState = PopUp.Close },
                    currentApiKey = apiKey
                )
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    GravitationSimulation2DTheme(darkTheme = isSystemInDarkTheme()) {
        GravitationSimulation2DApp(
            viewModel = object : HomeViewModelAbstract {
                override val simulationRecordFlow: Flow<List<SimulationRecord>>
                    get() = flowOf(listOf())
                override var state: NasaState
                    get() = NasaState()
                    set(value) {}
                override fun addRecord(record: SimulationRecord) {}
                override fun updateRecord(record: SimulationRecord) {}
                override fun deleteRecord(record: SimulationRecord) {}
                override fun setCurrentDate() {}
                override fun loadNasaDto(api_key: String, date: String) {}
            },
            context = ComponentActivity()
        )
    }
}


