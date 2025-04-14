package com.nomnomapp.nomnom.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.data.local.DatabaseProvider
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.viewmodel.RecipeDetailViewModel
import com.nomnomapp.nomnom.viewmodel.ShoppingListViewModel
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailScreen(
    mealId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val shoppingListViewModel: ShoppingListViewModel = remember { ShoppingListViewModel(context) }

    val viewModel: RecipeDetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val localRepo = LocalRecipeRepository(db.userRecipeDao())
                val apiRepo = RecipeRepository(db.cachedRecipeDao())
                return RecipeDetailViewModel(
                    apiRepository = apiRepo,
                    localRepository = localRepo
                ) as T
            }
        }
    )

    val mealState by viewModel.mealState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.loadMeal(mealId)
    }

    MealDetailContent(
        meal = mealState,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onBackClick = { navController.popBackStack() },
        onDelete = { recipeId ->
            viewModel.deleteUserRecipeById(recipeId) {
                navController.popBackStack()
            }
        },
        navController = navController,
        shoppingListViewModel = shoppingListViewModel
    )
}

@Composable
fun MealDetailContent(
    meal: Recipe?,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onDelete: (Int) -> Unit,
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModel
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun shareRecipe(recipe: Recipe) {
        val shareText = buildString {
            append("📖 ${recipe.title}\n")
            append("🍝 Category: ${recipe.category}\n")
            append("🌍 Cuisine: ${recipe.area}\n\n")
            append("🧾 Ingredients:\n")
            recipe.ingredients.forEach { append("- $it\n") }
            append("\n📋 Instructions:\n${recipe.instructions}")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, recipe.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share recipe via"))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            when {
                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                meal != null -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            ) {
                                Image(
                                    painter = rememberImagePainter(
                                        data = if (!meal.imageUrl.isNullOrEmpty()) meal.imageUrl else R.drawable.recipe_placeholder,
                                        builder = {
                                            crossfade(true)
                                            placeholder(R.drawable.recipe_placeholder)
                                            error(R.drawable.recipe_placeholder)
                                        }
                                    ),
                                    contentDescription = meal.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                            }
                        }

                        item {
                            Text(
                                text = meal.title,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                                    .padding(horizontal = 16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 16.dp)
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                AssistChip(onClick = {}, label = {
                                    Text("Category: ${meal.category}")
                                })
                                AssistChip(onClick = {}, label = {
                                    Text("Cuisine: ${meal.area}")
                                })
                            }
                            Divider()
                        }
                        item {
                            Text(
                                text = "Instructions",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = meal.instructions,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Divider(modifier = Modifier.padding(top = 16.dp))
                        }
                        item {
                            Text(
                                text = "Ingredients",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(meal.ingredients) { ingredient ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(15.dp))
                                    .clickable(onClick = {
                                        shoppingListViewModel.addOrRemoveItem(ingredient)
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Added $ingredient to shopping list",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    })
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ingredient, color = MaterialTheme.colorScheme.background)
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        meal?.ingredients?.forEach { ingredient ->
                                            shoppingListViewModel.addOrRemoveItem(ingredient)
                                        }
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Added all ingredients to shopping list",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Add all ingredients")
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onBackClick() },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f))
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { shareRecipe(meal) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            if (meal.id.startsWith("user_")) {
                                Box {
                                    IconButton(
                                        onClick = { menuExpanded = true },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f))
                                    ) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                                    }

                                    DropdownMenu(
                                        expanded = menuExpanded,
                                        onDismissRequest = { menuExpanded = false },
                                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Edit") },
                                            onClick = {
                                                menuExpanded = false
                                                val id = meal.id.removePrefix("user_").toIntOrNull()
                                                if (id != null) {
                                                    navController.navigate(Routes.editRecipeRoute(id))
                                                }
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {
                                                menuExpanded = false
                                                showDeleteDialog = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (showDeleteDialog && meal.id.startsWith("user_")) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text("Delete Recipe") },
                            text = { Text("Are you sure you want to delete this recipe?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDeleteDialog = false
                                    val id = meal.id.removePrefix("user_").toIntOrNull()
                                    if (id != null) {
                                        onDelete(id)
                                    }
                                }) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
                else -> Text(
                    text = "No meal found",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
