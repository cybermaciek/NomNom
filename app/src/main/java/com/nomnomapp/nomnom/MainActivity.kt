package com.nomnomapp.nomnom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nomnomapp.nomnom.ui.screens.*
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.MealDetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Możesz zachować enableEdgeToEdge(), jeśli potrzebujesz efektu immersion layoutu.
        enableEdgeToEdge()

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

                    composable("shoppingList") {
                        ShoppingListScreen()
                    }

                    composable("recipes") {
                        RecipeListScreen(
                            onNavigateToMealDetail = { mealId ->
                                navController.navigate("mealDetail/$mealId")
                            }
                        )
                    }


                    composable("settings") {
                        SettingsScreen()
                    }

                    composable(
                        route = "mealDetail/{mealId}",
                        arguments = listOf(
                            navArgument("mealId") { type = NavType.StringType }
                        )
                    ) {
                        val mealId = it.arguments?.getString("mealId") ?: ""
                        // Pobieramy MealDetailViewModel
                        val viewModel: MealDetailViewModel = viewModel()
                        // Wyświetlamy ekran detali z przekazanym mealId
                        MealDetailScreen(
                            mealId = mealId,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
