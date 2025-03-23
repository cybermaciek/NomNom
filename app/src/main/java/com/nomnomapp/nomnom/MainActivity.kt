package com.nomnomapp.nomnom

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nomnomapp.nomnom.view.screen.HomeScreen
import com.nomnomapp.nomnom.view.screen.RecipeListScreen
import com.nomnomapp.nomnom.view.screen.ShoppingListScreen


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



