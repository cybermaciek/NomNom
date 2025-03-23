package com.nomnomapp.nomnom.view.screen

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.viewmodel.RecipeListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nomnomapp.nomnom.ui.theme.NomNomTheme


//Todo: zrobić aby trzymac Set<String> z ID przepisów w ViewModel
//Todo: po kliknięciu serca — przepis trafia do ulubionych (później można to zsynchronizować z Roomem lub serwerem)
//Todo: Możliwość dodawania własnych przepisów, na dole po prawej strnie button i przenosi nas do ekranu w ktym mamy szablon



class RecipeListScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NomNomTheme {
                RecipeListScreenContent()
            }
        }
    }

    @Composable
    fun RecipeListScreenContent(viewModel: RecipeListViewModel = viewModel()) {
        val recipes by viewModel.recipes.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.searchRecipes("Chicken")
        }

        RecipeListScreenView(recipes)
    }

    @Composable
    fun RecipeListScreenView(recipes: List<Recipe>) {
        var search by remember { mutableStateOf("") }
        val favoriteIds = remember { mutableStateListOf<String>() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Pasek górny
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Wróć", tint = MaterialTheme.colorScheme.onBackground)
                Text("Przepisy", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Ustawienia", tint = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pole wyszukiwania
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Szukaj...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Przycisk Ulubione i Historia
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton(text = "Ulubione", icon = Icons.Outlined.Favorite)
                FilterButton(text = "Historia", icon = Icons.Outlined.History)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredRecipes = recipes.filter {
                it.title.contains(search, ignoreCase = true)
            }

            // Lista przepisów
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        isFavorite = favoriteIds.contains(recipe.id),
                        onFavoriteClick = {
                            if (favoriteIds.contains(it.id)) {
                                favoriteIds.remove(it.id)
                            } else {
                                favoriteIds.add(it.id)
                            }
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
            Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, fontSize = 14.sp)
        }
    }

    @Composable
    fun RecipeCard(recipe: Recipe, isFavorite: Boolean, onFavoriteClick: (Recipe) -> Unit) {
        Column {
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
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f), shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Ulubione",
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
    fun LightmodePreview() {
        NomNomTheme {
            RecipeListScreenContent()
        }
    }

    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
    @Composable
    fun DarkmodePreview() {
        NomNomTheme {
            RecipeListScreenContent()
        }
    }
}