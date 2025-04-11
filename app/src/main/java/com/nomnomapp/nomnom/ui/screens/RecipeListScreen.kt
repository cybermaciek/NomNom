package com.nomnomapp.nomnom.ui.screens

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import com.nomnomapp.nomnom.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nomnomapp.nomnom.data.local.DatabaseProvider
import com.nomnomapp.nomnom.data.repository.FavoriteRepository
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository

@Composable
fun RecipeListScreen(
    navController: NavController,
    onNavigateToMealDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }

    val viewModel: RecipeListViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val localRepo = LocalRecipeRepository(db.userRecipeDao())
                val favoriteRepo = FavoriteRepository(db.favoriteDao())
                val recipeRepo = RecipeRepository(db.cachedRecipeDao())
                return RecipeListViewModel(localRepo, recipeRepo, favoriteRepo) as T
            }
        }
    )

    val recipes by viewModel.recipes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllRecipes()
        viewModel.loadFilters()
    }

    RecipeListScreenView(
        recipes = recipes,
        onNavigateToMealDetail = onNavigateToMealDetail,
        navController = navController,
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onSettingsClick = { navController.navigate(Routes.SETTINGS.route) }
    )
}

@Composable
fun RecipeListScreenView(
    recipes: List<Recipe>,
    onNavigateToMealDetail: (String) -> Unit,
    navController: NavController,
    viewModel: RecipeListViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    var showOnlyUserRecipes by remember { mutableStateOf(false) }

    val categories by viewModel.categories.collectAsState()
    val areas by viewModel.areas.collectAsState()
    val usingCache by viewModel.usingCache.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(usingCache) {
        if (usingCache) snackbarHostState.showSnackbar("No internet - showing data from memory")
    }

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedArea by remember { mutableStateOf("All") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filteredRecipes = recipes.filter { recipe ->
        recipe.title.contains(search, ignoreCase = true) &&
                (selectedCategory == "All" || recipe.category == selectedCategory) &&
                (selectedArea == "All" || recipe.area == selectedArea) &&
                (!showOnlyFavorites || favoriteIds.contains(recipe.id)) &&
                (!showOnlyUserRecipes || recipe.id.startsWith("user_"))
    }

    LaunchedEffect(search) {
        if (search.isNotBlank()) viewModel.searchRecipes(search) else viewModel.searchRecipes("")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_RECIPE.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add Recipe")
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Recipes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { onSettingsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = search,
                onValueChange = { search = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (search.isBlank()) {
                                Text("Search...", color = MaterialTheme.colorScheme.onBackground)
                            }
                            innerTextField()
                        }
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip("Favourites", showOnlyFavorites) {
                        showOnlyFavorites = !showOnlyFavorites
                    }
                }
                item {
                    FilterChip("My Recipes", showOnlyUserRecipes) {
                        showOnlyUserRecipes = !showOnlyUserRecipes
                    }
                }
                item {
                    FilterDropdown("Category", categories, selectedCategory) {
                        selectedCategory = it
                    }
                }
                item {
                    FilterDropdown("Cuisine: ", areas, selectedArea) {
                        selectedArea = it
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        isFavorite = favoriteIds.contains(recipe.id),
                        onFavoriteClick = { viewModel.toggleFavorite(it.id) },
                        onCardClick = { clicked ->
                            onNavigateToMealDetail(clicked.id)
                        },
                        onDeleteClick = { clickedRecipe ->
                            val id = recipe.id.removePrefix("user_").toIntOrNull()
                            if (id != null) {
                                viewModel.deleteUserRecipeById(id) { /* opcjonalny onSuccess */ }
                            }
                        }
                    )

                }
            }

        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: (Recipe) -> Unit,
    onCardClick: (Recipe) -> Unit,
    onDeleteClick: (Recipe) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.clickable { onCardClick(recipe) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (recipe.id.startsWith("user_")) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    menuExpanded = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
                AnimatedFavoriteIcon(
                    isFavorite = isFavorite,
                    onToggle = { onFavoriteClick(recipe) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            recipe.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(modifier = Modifier.padding(4.dp))

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Recipe") },
                text = { Text("Are you sure you want to delete this recipe?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        onDeleteClick(recipe)
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
}



@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(label, fontSize = 14.sp)
    }
}

@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(40.dp)
        ) {
            Text("$label: $selectedOption",
                fontSize = 14.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AnimatedFavoriteIcon(
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    val context = LocalContext.current

    val scale = remember { Animatable(1f) }
    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(isFavorite) {
        if (isFavorite) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(durationMillis = 150)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 150)
            )
        }
    }

    Box(
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .clip(CircleShape)
            .clickable {
                onToggle()
                MediaPlayer.create(context, R.raw.pop)?.apply {
                    setOnCompletionListener { release() }
                    start()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = heartColor
        )
    }
}





@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
@Composable
fun Recipe_List_LightmodePreview() {
    NomNomTheme {
        RecipeListScreen(onNavigateToMealDetail = {}, navController = NavController(LocalContext.current))
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun Recipe_List_DarkmodePreview() {
    NomNomTheme {
        RecipeListScreen(onNavigateToMealDetail = {}, navController = NavController(LocalContext.current))
    }
}
