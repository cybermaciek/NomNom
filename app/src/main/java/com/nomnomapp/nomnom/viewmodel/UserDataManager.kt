package com.nomnomapp.nomnom.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.io.FileOutputStream

object UserDataManager {
    private const val USER_NAME_KEY = "user_name"
    private const val USER_IMAGE_FILE = "user_image.jpg"

    var userName by mutableStateOf("")
    var userBitmap by mutableStateOf<Bitmap?>(null)

    fun loadUserData(context: Context) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userName = sharedPref.getString(USER_NAME_KEY, "") ?: ""
        val file = File(context.filesDir, USER_IMAGE_FILE)
        if (file.exists()) {
            userBitmap = BitmapFactory.decodeFile(file.absolutePath)
        }
    }

    fun saveUserData(context: Context, name: String, bitmap: Bitmap) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(USER_NAME_KEY, name)
            apply()
        }

        val file = File(context.filesDir, USER_IMAGE_FILE)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        userName = name
        userBitmap = bitmap
    }

    private const val THEME_PREF_KEY = "app_theme"
    var currentTheme by mutableStateOf(AppTheme.SYSTEM)

    enum class AppTheme(val value: String) {
        LIGHT("Light"),
        DARK("Dark"),
        SYSTEM("System")
    }

    fun loadThemePreference(context: Context) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val themeValue = sharedPref.getString(THEME_PREF_KEY, AppTheme.SYSTEM.value)
        currentTheme = AppTheme.values().firstOrNull { it.value == themeValue } ?: AppTheme.SYSTEM
    }

    fun saveThemePreference(context: Context, theme: AppTheme) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(THEME_PREF_KEY, theme.value)
            apply()
        }
        currentTheme = theme
    }
}
