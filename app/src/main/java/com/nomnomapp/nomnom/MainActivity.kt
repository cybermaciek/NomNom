package com.nomnomapp.nomnom

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nomnomapp.nomnom.ui.screens.HomeScreen
import com.nomnomapp.nomnom.ui.screens.RecipeListScreen
import com.nomnomapp.nomnom.ui.screens.SettingsScreen
import com.nomnomapp.nomnom.ui.screens.ShoppingListScreen
import com.nomnomapp.nomnom.ui.theme.NomNomTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NomNomTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onNavigateToRecipes = { navController.navigate("recipes") },
                            onNavigateToSettings = { navController.navigate("settings") },
                            onNavigateToShoppingList = { navController.navigate("shoppingList") }
                        )
                    }
                    composable("shoppingList") { ShoppingListScreen() }
                    composable("recipes") { RecipeListScreen() }
                    composable("settings") { SettingsScreen() }
                }
            }
        }
    }
}
