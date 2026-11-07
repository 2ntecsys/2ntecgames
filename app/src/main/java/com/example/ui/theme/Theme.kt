package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = KidsSkyBlueDark,
    onPrimary = KidsSurfaceLight,
    primaryContainer = KidsSkyBlue,
    secondary = KidsAmberOrange,
    onSecondary = KidsSurfaceLight,
    tertiary = KidsMintGreen,
    background = KidsBackgroundLight,
    surface = KidsSurfaceLight,
    surfaceVariant = KidsSunnyYellow,
    onSurface = KidsDeepIndigo
)

private val DarkColorScheme = darkColorScheme(
    primary = KidsSkyBlue,
    onPrimary = KidsBackgroundDark,
    primaryContainer = KidsSkyBlueDark,
    secondary = KidsAmberOrange,
    onSecondary = KidsBackgroundDark,
    tertiary = KidsMintGreen,
    background = KidsBackgroundDark,
    surface = KidsSurfaceDark,
    surfaceVariant = KidsDeepIndigo,
    onSurface = KidsSurfaceLight
)

val KidsShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun AnimalShadowPuzzleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KidsTypography,
        shapes = KidsShapes,
        content = content
    )
}
