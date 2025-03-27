package com.nomnomapp.nomnom


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
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
import com.nomnomapp.nomnom.viewmodel.UserDataManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Load user data when app starts
        UserDataManager.loadUserData(this)

        setContent {
            NomNomTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // Check if user has already set up their profile
                val startDestination = if (UserDataManager.userName.isNotBlank() && UserDataManager.userBitmap != null) {
                    Routes.HOME.route
                } else {
                    Routes.CREATE_USER.route
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
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

                    composable(Routes.RECIPES.route) {
                        RecipeListScreen(
                            navController = navController,
                            onNavigateToMealDetail = { mealId ->
                                navController.navigate(Routes.mealDetailRoute(mealId))
                            }
                        )
                    }

                    composable(Routes.SETTINGS.route) {
                        SettingsScreen()
                    }

                    composable(
                        route = Routes.MEAL_DETAIL.route,
                        arguments = listOf(navArgument("mealId") { type = NavType.StringType })
                    ) { backStackEntry ->  // Fixed: Added proper parameter name
                        val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
                        val viewModel: RecipeDetailViewModel = viewModel()
                        RecipeDetailScreen(
                            mealId = mealId,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    composable(Routes.ADD_RECIPE.route) {
                        AddRecipeScreen(
                            navController = navController
                        )
                    }

                    composable(Routes.CREATE_USER.route) {
                            CreateUserScreen(
                                onNavigateToHome = {
                                    navController.navigate(Routes.HOME.route) {
                                        popUpTo(Routes.CREATE_USER.route) { inclusive = true }
                                    }
                                },
                                context = context
                            )
                    }
                }
            }
        }
    }
}