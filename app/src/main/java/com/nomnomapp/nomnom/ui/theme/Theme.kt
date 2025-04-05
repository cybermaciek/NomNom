package com.nomnomapp.nomnom.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nomnomapp.nomnom.viewmodel.UserDataManager

private val LightColorScheme = lightColorScheme(
    background = appWhite,
    onBackground = appBlack,
    primary = appOrange,
    secondary = appGreen,
    tertiary = appBlue
)

private val DarkColorScheme = darkColorScheme(
    background = appBlack,
    onBackground = appWhite,
    primary = appOrange,
    secondary = appGreen,
    tertiary = appBlue
)

@Composable
fun NomNomTheme(
    // Remove the default darkTheme parameter since we'll use our own logic
    dynamicColor: Boolean = false, // Keep dynamic color disabled as per your comment
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    // Load the theme preference if not already loaded
    if (UserDataManager.currentTheme == UserDataManager.AppTheme.SYSTEM) {
        UserDataManager.loadThemePreference(context)
    }

    val systemTheme = isSystemInDarkTheme()
    val darkTheme = when (UserDataManager.currentTheme) {
        UserDataManager.AppTheme.LIGHT -> false
        UserDataManager.AppTheme.DARK -> true
        UserDataManager.AppTheme.SYSTEM -> systemTheme
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}