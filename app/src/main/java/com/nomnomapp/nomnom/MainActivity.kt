package com.nomnomapp.nomnom

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nomnomapp.nomnom.ui.screens.HomeScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DebugMenuScreen()
        }
    }

    @Composable
    fun DebugMenuScreen() {
        val context = LocalContext.current
        context.startActivity(Intent(context, HomeScreen::class.java))
    }
}



