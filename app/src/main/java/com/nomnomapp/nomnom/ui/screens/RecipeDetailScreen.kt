package com.nomnomapp.nomnom.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.nomnomapp.nomnom.model.Recipe
import com.nomnomapp.nomnom.viewmodel.RecipeDetailViewModel
import com.nomnomapp.nomnom.ui.theme.NomNomTheme


@Composable
fun RecipeDetailScreen(
    mealId: String,
    viewModel: RecipeDetailViewModel,
    navController: NavController // Dodaj parametr navController
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
        errorMessage = errorMessage,
        onBackClick = {
            // Po kliknięciu strzałki cofamy się
            navController.popBackStack()
        }
    )
}

@Composable
fun MealDetailContent(
    meal: Recipe?,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit
) {
    Scaffold(){ contentPadding ->
        Box(modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
            )
        {
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
                                    painter = rememberImagePainter(meal.imageUrl),
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
                                    .background(Color.Gray, shape = RoundedCornerShape(15.dp))
                                    .clickable(onClick = {})
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ingredient, color = Color.White)
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.onBackground
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
                                FilledTonalButton(onClick = { }) {
                                    Text("Add all ingredients")
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
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
        ) {}
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
        ) {}
    }
}