package com.nomnomapp.nomnom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.ui.screens.*
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.RecipeDetailViewModel

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
                    startDestination = Routes.HOME.route
                ) {
                    composable(Routes.HOME.route) {
                        HomeScreen(
                            onNavigateToRecipes = { navController.navigate(Routes.RECIPES.route) },
                            onNavigateToSettings = { navController.navigate(Routes.SETTINGS.route) },
                            onNavigateToShoppingList = { navController.navigate(Routes.SHOPPING_LIST.route)}
                        )
                    }

                    composable(Routes.SHOPPING_LIST.route) {
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
                        val viewModel: RecipeDetailViewModel = viewModel()
                        // Wyświetlamy ekran detali z przekazanym mealId
                        RecipeDetailScreen(
                            mealId = mealId,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
