package com.nomnomapp.nomnom.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.RecipeListViewModel

@Composable
fun RecipeListScreen(
    navController: NavController,
    onNavigateToMealDetail: (String) -> Unit,
    viewModel: RecipeListViewModel = viewModel()
) {
    val recipes by viewModel.recipes.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.searchRecipes("Chicken")
    }
    RecipeListScreenView(
        recipes = recipes,
        onNavigateToMealDetail = onNavigateToMealDetail,
        navController = navController
    )

}

@Composable
fun RecipeListScreenView(
    recipes: List<Recipe>,
    onNavigateToMealDetail: (String) -> Unit,
    navController: NavController
) {
    var search by remember { mutableStateOf("") }
    val favoriteIds = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp, top = 20.dp)
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
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "Recipes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = search,
            onValueChange = { search = it },
            singleLine = true,
            maxLines = 1,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.LightGray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (search.isBlank()) {
                            Text(
                                text = "Search...",
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterButton(text = "Favourites", icon = Icons.Outlined.Favorite)
            FilterButton(text = "History", icon = Icons.Outlined.History)
        }
        Spacer(modifier = Modifier.height(16.dp))
        val filteredRecipes = recipes.filter {
            it.title.contains(search, ignoreCase = true)
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(filteredRecipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    isFavorite = favoriteIds.contains(recipe.id),
                    onFavoriteClick = { clickedRecipe ->
                        if (favoriteIds.contains(clickedRecipe.id)) {
                            favoriteIds.remove(clickedRecipe.id)
                        } else {
                            favoriteIds.add(clickedRecipe.id)
                        }
                    },
                    onCardClick = { clickedRecipe ->
                        onNavigateToMealDetail(clickedRecipe.id)
                    }
                )
            }
        }
    }
}

@Composable
fun FilterButton(text: String, icon: ImageVector) {
    OutlinedButton(
        onClick = { },
        shape = RoundedCornerShape(50),
        modifier = Modifier.height(40.dp)
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: (Recipe) -> Unit,
    onCardClick: (Recipe) -> Unit
) {
    Column(
        modifier = Modifier.clickable { onCardClick(recipe) }
    ) {
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
            IconButton(
                onClick = { onFavoriteClick(recipe) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favourites icon",
                    tint = Color.Red
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = recipe.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
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
