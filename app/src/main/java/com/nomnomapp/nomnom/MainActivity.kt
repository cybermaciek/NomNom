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
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text("Ekrany testowe", style = MaterialTheme.typography.headlineSmall)
//
//
//            Button(onClick = {
//                context.startActivity(Intent(context, ShoppingListScreen::class.java))
//            }) {
//                Text("Shopping List Activity")
//            }
//
//            Button(onClick = {
//                context.startActivity(Intent(context, RecipeListScreen::class.java))}) {
//                Text("Recipes Activity")
//            }
//
//            Button(onClick = {}) {
//                Text("Onboarding Activity")
//            }
//
//
//            Button(onClick = {}) {
//                Text("Profile Activity")
//            }
//
//
//
//        }
    }
}



