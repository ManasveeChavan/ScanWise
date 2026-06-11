package com.scanwise.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = ScanWiseBlue,
    secondary = ScanWiseRed,
    tertiary = ScanWiseGreen,
    background = LightBackground,
    surface = LightSurface,
    error = ScanWiseError,
)

private val DarkColors = darkColorScheme(
    primary = ScanWiseBlue,
    secondary = ScanWiseRed,
    tertiary = ScanWiseGreen,
    background = DarkBackground,
    surface = DarkSurface,
    error = ScanWiseError,
)

@Composable
fun ScanWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
