package com.example.gravitationsimulation2d.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    background = Cyan900,
    surface = Cyan700,
    onSurface = Grey100,
    primary = buttonDarkColor,
    onPrimary = Grey100,
    secondary = Grey100,
    onSecondary = White
)

private val LightColorPalette = lightColors(
    background = Green100,
    surface = Green50,
    onPrimary = Grey900,
    onSurface = Grey900,
    primary = buttonLightColor,
    secondary = Grey700,
    onSecondary = Black
)

@Composable
fun GravitationSimulation2DTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}