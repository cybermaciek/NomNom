package com.nomnomapp.nomnom.ui.screens

import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import com.nomnomapp.nomnom.R


@Composable
fun RecipeListScreen(
    navController: NavController,
    onNavigateToMealDetail: (String) -> Unit,
    viewModel: RecipeListViewModel = viewModel()
) {
    val recipes by viewModel.recipes.collectAsState()

    LaunchedEffect(Unit) {
        //viewModel.searchRecipes("Chicken")
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
    val favoriteIds = remember { mutableStateListOf<String>() }
    var showOnlyUserRecipes by remember { mutableStateOf(false) }


    val categories by viewModel.categories.collectAsState()
    val areas by viewModel.areas.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedArea by remember { mutableStateOf("All") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filteredRecipes = recipes.filter { recipe ->
        recipe.title.contains(search, ignoreCase = true) &&
                (selectedCategory == "All" || recipe.category == selectedCategory) &&
                (selectedArea == "All" || recipe.area == selectedArea) &&
                (!showOnlyFavorites || favoriteIds.contains(recipe.id)) &&
                (!showOnlyUserRecipes || recipe.id.startsWith("user_")) // zakładamy że własne przepisy mają np. prefix "user_"
    }


    LaunchedEffect(search) {
        if (search.isNotBlank()) {
            viewModel.searchRecipes(search)
        } else {
            viewModel.searchRecipes("")
        }
    }

    Scaffold(
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
                        onFavoriteClick = { clicked ->
                            if (favoriteIds.contains(clicked.id)) {
                                favoriteIds.remove(clicked.id)
                            } else {
                                favoriteIds.add(clicked.id)
                            }
                        },
                        onCardClick = { clicked ->
                            onNavigateToMealDetail(clicked.id)
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
    onCardClick: (Recipe) -> Unit
) {
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
            Box(modifier = Modifier
                .align(Alignment.TopEnd)
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                AnimatedFavoriteIcon(
                    isFavorite = isFavorite,
                    onToggle = { onFavoriteClick(recipe) }
                )

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(recipe.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
        HorizontalDivider(modifier = Modifier.padding(4.dp))
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
    var triggerExplosion by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.5f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground
    )

    // Okrągłe tło
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .clip(CircleShape)
            .clickable {
                onToggle()
                triggerExplosion = true

                // Odtwarzanie dźwięku
                MediaPlayer.create(context, R.raw.pop)?.apply {
                    setOnCompletionListener { release() }
                    start()
                }
            }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favourite",
            tint = heartColor
        )

        if (triggerExplosion) {
            LaunchedEffect(Unit) {
                delay(500)
                triggerExplosion = false
            }
            HeartExplosionAnimation()
        }
    }
}

@Composable
fun HeartExplosionAnimation() {
    val hearts = remember { List(6) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        hearts.forEachIndexed { index, anim ->
            launch {
                delay(index * 50L)
                anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 400)
                )
            }
        }
    }

    hearts.forEachIndexed { index, anim ->
        val angle = 100f * index // rozrzut serduszek

        val offsetX = anim.value * cos(Math.toRadians(angle.toDouble())).toFloat() * 40
        val offsetY = -anim.value * sin(Math.toRadians(angle.toDouble())).toFloat() * 40

        Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = null,
            tint = Color.Red.copy(alpha = 1f - anim.value),
            modifier = Modifier
                .offset(x = offsetX.dp, y = offsetY.dp)
                .size((16 + 8 * (1 - anim.value)).dp)
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
