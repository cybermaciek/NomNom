package com.nomnomapp.nomnom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.viewmodel.MealDetailViewModel
import com.nomnomapp.nomnom.ui.theme.NomNomTheme

@Composable
fun MealDetailScreen(
    mealId: String,
    viewModel: MealDetailViewModel
) {
    val mealState by viewModel.mealState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(mealId) {
        viewModel.loadMeal(mealId)
    }

    MealDetailContent(
        meal = mealState,
        isLoading = isLoading,
        errorMessage = errorMessage
    )
}

@Composable
fun MealDetailContent(
    meal: Recipe?,
    isLoading: Boolean,
    errorMessage: String?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            meal != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                            Text(
                                text = meal.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(0.dp))
                        ) {
                            Image(
                                painter = rememberImagePainter(meal.imageUrl),
                                contentDescription = meal.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                    }
                    item {
                        Text(
                            text = "Category: ${meal.category}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        Text(
                            text = "Area: ${meal.area}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        Text(
                            text = meal.instructions,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = "Ingredients:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    items(meal.ingredients) { ingredient ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• $ingredient",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            OutlinedButton(
                                onClick = { },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.onBackground)
                            }
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
                                onClick = { },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("Add all ingredients")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            else -> {
                Text(
                    text = "No meal found",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO, name = "Light")
@Composable
fun MealDetailContentLightPreview() {
    NomNomTheme {
        MealDetailContent(
            meal = Recipe(
                id = "52772",
                title = "Spaghetti Carbonara",
                imageUrl = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
                category = "Pasta",
                area = "Italian",
                instructions = "1. Cook pasta\n2. Mix eggs and cheese\n3. Add pancetta\n4. Combine everything",
                ingredients = listOf("Spaghetti", "Eggs", "Cheese", "Pancetta")
            ),
            isLoading = false,
            errorMessage = null
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun MealDetailContentDarkPreview() {
    NomNomTheme {
        MealDetailContent(
            meal = Recipe(
                id = "52772",
                title = "Spaghetti Carbonara",
                imageUrl = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
                category = "Pasta",
                area = "Italian",
                instructions = "1. Cook pasta\n2. Mix eggs and cheese\n3. Add pancetta\n4. Combine everything",
                ingredients = listOf("Spaghetti", "Eggs", "Cheese", "Pancetta")
            ),
            isLoading = false,
            errorMessage = null
        )
    }
}
