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
        // Load name from SharedPreferences
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        userName = sharedPref.getString(USER_NAME_KEY, "") ?: ""

        // Load image from internal storage
        val file = File(context.filesDir, USER_IMAGE_FILE)
        if (file.exists()) {
            userBitmap = BitmapFactory.decodeFile(file.absolutePath)
        }
    }

    fun saveUserData(context: Context, name: String, bitmap: Bitmap) {
        // Save name to SharedPreferences
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(USER_NAME_KEY, name)
            apply()
        }

        // Save image to internal storage
        val file = File(context.filesDir, USER_IMAGE_FILE)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        // Update current values
        userName = name
        userBitmap = bitmap
    }
}
