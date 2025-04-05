package com.nomnomapp.nomnom.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nomnomapp.nomnom.viewmodel.UserDataManager

class SettingsViewModel : ViewModel() {
    var selectedTheme by mutableStateOf(UserDataManager.currentTheme.value)
        private set

    fun updateTheme(theme: String, context: Context) {
        selectedTheme = theme
        val appTheme = when (theme) {
            "Light" -> UserDataManager.AppTheme.LIGHT
            "Dark" -> UserDataManager.AppTheme.DARK
            else -> UserDataManager.AppTheme.SYSTEM
        }
        UserDataManager.saveThemePreference(context, appTheme)
    }
}