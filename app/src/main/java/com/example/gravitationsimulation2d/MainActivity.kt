package com.example.gravitationsimulation2d

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gravitationsimulation2d.data.Datasource
import com.example.gravitationsimulation2d.ui.theme.GravitationSimulation2DTheme
import kotlin.math.pow


/*
State in Jetpack Composable
https://developer.android.com/codelabs/jetpack-compose-state#8

TODO LIST
when the screen is rotated, planets disappear
colors of the app bar and the status bar must be same
enumeration of planets in the simulation screen
2 planet can be placed to the same position if one of them is modified later

Remove unnecessary parts
Add orbit dots
 */

const val CELESTIAL_BODY_INIT_SCREEN: Int = 0
const val SIMULATION_SCREEN: Int = 1

val data_source: Datasource = Datasource()
val borderColor = Color(red = 105, green = 205, blue = 216)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GravitationSimulation2DTheme {
                GravitationSimulation2DApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GravitationSimulation2DApp() {
    var currentScreen by rememberSaveable {
        mutableStateOf(CELESTIAL_BODY_INIT_SCREEN)
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

    if (currentScreen == CELESTIAL_BODY_INIT_SCREEN) {
        Scaffold(
            topBar = {
                AppTopBarInitScreen(
                    { addPlanetToList(planets) },                       // add planet button
                    { deletePlanetFromList(planets)                     // delete planet button
                        data_source.nullAllPrevInputs()
                        selectedPlanetChange = !selectedPlanetChange
                    },
                    {
                        changeValuesOf(planets)                         // change planet button
                        val planet: CelestialBody? = getSelectedInitialisedPlanet(planets)
                        data_source.updateSettingTextsPrevInputs(planet)
                        selectedPlanetChange = !selectedPlanetChange
                    },
                    {
                        currentScreen = SIMULATION_SCREEN                 // run simulation button
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
    else if (currentScreen == SIMULATION_SCREEN) {
        Scaffold(
            topBar = {
                AppTopBarSimulationScreen(
                    planets,
                    { currentScreen = CELESTIAL_BODY_INIT_SCREEN // back to the initialisation button
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

@Composable
fun AppTopBarInitScreen(addPlanet: () -> Unit, deletePlanet: () -> Unit, changePlanet: () -> Unit, runSimulation: () -> Unit ,modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null
        )
        IconButton(onClick = { addPlanet() }) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.add),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { deletePlanet() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { changePlanet() }) {
            Icon(
                imageVector = Icons.Filled.ChangeCircle,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.change),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { runSimulation() }) {
            Icon(
                imageVector = Icons.Filled.PlayCircle,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.run),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AppTopBarSimulationScreen(planets: MutableList<CelestialBody>, backToInitialisation: () -> Unit, onPlayPauseClick: () -> Unit, onRestartClick: () -> Unit, current_screen: Int, modifier: Modifier = Modifier) {
    var playing by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { backToInitialisation() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.back_to_initialisation),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { onPlayPauseClick(); playing = !playing }) {
            Icon(
                imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.play_pause),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(onClick = { onRestartClick(); playing = false }) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                tint = MaterialTheme.colors.secondary,
                contentDescription = stringResource(R.string.restart),
                modifier = Modifier.size(24.dp)
            )
        }
        DrawInitialisedPlanetList(planets, current_screen, Color.Green, {  })
    }
}

@Composable
fun DrawInitialisedPlanetList(planets: MutableList<CelestialBody>, current_screen: Int, planetBackGroundColor: Color, informUp: () -> Unit, modifier: Modifier = Modifier) {
    var state by rememberSaveable {
        mutableStateOf(false)
    }
    LazyRow(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .size(48.dp)
            .border(
                width = 4.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(planets) {celestial_body ->
            DrawInitialisedPlanetImage(
                celestial_body,
                planetBackGroundColor,
                state,
                {
                    state = !state
                    if (current_screen == CELESTIAL_BODY_INIT_SCREEN) {
                        val prevSelected = celestial_body.selected
                        unselectAllPlanets(planets)
                        celestial_body.selected = !prevSelected
                    } else if (current_screen == SIMULATION_SCREEN) {
                        celestial_body.selected = !celestial_body.selected
                    }
                    informUp()
                }
            )
        }
    }
}

@Composable
fun DrawInitialisedPlanetImage(celestial_body: CelestialBody, backgroundColor: Color, state: Boolean, onChange: () -> Unit, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = celestial_body.imageId),
        contentDescription = null,
        modifier =
        if (celestial_body.selected) modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .background(backgroundColor)
            .clickable { onChange() }
        else modifier.clickable { onChange() }
    )
}

@Composable
fun PlanetImageCard(modifier: Modifier = Modifier) {
    Card (
        modifier = modifier.padding(4.dp),
        elevation = 4.dp
    ) {
        var state by rememberSaveable {
            mutableStateOf(false)
        }
        LazyRow(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .size(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(data_source.loadImages()) { planetImage ->
                DrawPlanetImage(planetImage, state, { data_source.selectImage(planetImage); state = !state })
            }
        }
    }
}

@Composable
fun DrawPlanetImage(planetImage: PlanetImage, state: Boolean, onChange: () -> Unit, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = planetImage.imageId),
        contentDescription = null,
        modifier =
        if (planetImage.selected) modifier
            .clickable { onChange() }
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
        else modifier.clickable { onChange() }
    )
}

@Composable
fun CelestialBodySettingTextList(state: Boolean, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.background(MaterialTheme.colors.background)
    ) {
        items(data_source.loadSettingTexts()) {settingText ->
            CelestialBodySettingText(settingText, state, { input -> settingText.setInput(input) })
        }
    }
}

@Composable
fun CelestialBodySettingText(settingText: SettingText, state: Boolean, onChange: (String) -> Boolean, modifier: Modifier = Modifier) {
    var input by rememberSaveable {
        mutableStateOf(settingText.userInput)
    }
    Card (
        modifier = modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = settingText.firstTextId),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { if (onChange(it)) { input = it } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                label = {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = settingText.textFiledHint,
                            fontSize = 10.sp,
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (settingText.prevInput != null) {
                            Text(
                                text = settingText.prevInput!!,
                                fontSize = 10.sp,
                                color = MaterialTheme.colors.onSecondary,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            )
            Text(
                text = stringResource(id = settingText.secondTextId),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DrawCelestialBody(planet: CelestialBody) {
    val planetSize = (planet.radius*2).dp
    Box(
        modifier = Modifier
            .offset(planet.xOffset, planet.yOffset)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = planet.imageId),
            modifier = Modifier.size(planetSize),
            contentDescription = null
        )
    }
}

@Composable
fun RunSimulation(simulation: Simulation, modifier: Modifier = Modifier) {
    val density = LocalDensity.current // use it to get results independent of screen aspects
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                simulation.update(it)
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clipToBounds()
    ) {
        simulation.planets.forEach {planet ->
            DrawCelestialBody(planet)
        }
    }
}

private fun addPlanetToList(planets: MutableList<CelestialBody>) {
    if (data_source.inputsAreValid()) {
        val userInputs = data_source.getUserInputs()
        val x: Double = userInputs[0].toDouble()
        val y: Double = userInputs[1].toDouble()
        val xVelocity: Double = userInputs[2].toDouble()
        val yVelocity: Double = userInputs[3].toDouble()
        val massCoef: Double = userInputs[4].toDouble()
        val massPower: Double = userInputs[5].toDouble()
        val imageId: Int = data_source.getSelectedImageId()
        if (!planets.any { planet -> planet.x == x && planet.y == y }) {
            planets.add(CelestialBody(x, y, xVelocity, yVelocity, massCoef, massPower, imageId, nextPlanetId++))
        }
    }
}

private fun deletePlanetFromList(planets: MutableList<CelestialBody>) {
    planets.removeAll { it.selected }
}

private fun unselectAllPlanets(planets: MutableList<CelestialBody>) {
    for (planet in planets) {
        planet.selected = false
    }
}

private fun getSelectedInitialisedPlanet(planets: MutableList<CelestialBody>): CelestialBody? {
    for (planet in planets) {
        if (planet.selected)
            return planet
    }
    return null
}

private fun changeValuesOf(planets: MutableList<CelestialBody>) {
    val selectedPlanet: CelestialBody? = getSelectedInitialisedPlanet(planets)
    if (selectedPlanet != null &&
        data_source.inputsAreValid() &&
        !planets.any { planet -> planet.id != selectedPlanet.id && planet.x == selectedPlanet.x && planet.y == selectedPlanet.y }
    ) {
        val userInputs = data_source.getUserInputs()
        selectedPlanet.x = userInputs[0].toDouble()
        selectedPlanet.y = userInputs[1].toDouble()
        selectedPlanet.xVelocity = userInputs[2].toDouble()
        selectedPlanet.yVelocity = userInputs[3].toDouble()
        selectedPlanet.massCoef = userInputs[4].toDouble()
        selectedPlanet.massPower = userInputs[5].toDouble()
        selectedPlanet.mass = selectedPlanet.massCoef * 10.0.pow(selectedPlanet.massPower)
        selectedPlanet.imageId = data_source.getSelectedImageId()
    }
}

@Preview
@Composable
fun AppPreview() {
    GravitationSimulation2DTheme(darkTheme = false) {
        GravitationSimulation2DApp()
    }
}

@Preview
@Composable
fun AppDarkThemePreview() {
    GravitationSimulation2DTheme(darkTheme = true) {
        GravitationSimulation2DApp()
    }
}

