package com.bishal.securepasswordmanager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val commonColorScheme = darkColorScheme(
    primary = blue1,
    secondary = blue2,
    tertiary = blue3,
    background = whiteBg,
    surface = whiteBg,
    onPrimary = white,
    onSecondary = white,
    onTertiary = white,
    onBackground = blackLight,
    onSurface = blackLight,
    )

@Composable
fun SecurePasswordManagerTheme(content: @Composable () -> Unit) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val colorScheme = commonColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
