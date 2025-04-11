package com.nomnomapp.nomnom.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.data.local.DatabaseProvider
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.FavoriteRepository
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.data.repository.RecipeRepository
import com.nomnomapp.nomnom.viewmodel.AddRecipeViewModel
import com.nomnomapp.nomnom.viewmodel.RecipeListViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController,
    editRecipeId: Int? = null
) {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val repository = remember { LocalRecipeRepository(db.userRecipeDao()) }
    val viewModel = remember { AddRecipeViewModel(recipeRepository = repository) }
    val recipeListViewModel: RecipeListViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val localRepo = LocalRecipeRepository(db.userRecipeDao())
                val favoriteRepo = FavoriteRepository(db.favoriteDao())
                val recipeRepo = RecipeRepository(db.cachedRecipeDao())
                return RecipeListViewModel(localRepo, recipeRepo, favoriteRepo) as T
            }
        }
    )

    var title by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val ingredientList = remember { mutableStateListOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var existingImagePath by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedArea by remember { mutableStateOf("") }

    val categories by recipeListViewModel.categories.collectAsState()
    val areas by recipeListViewModel.areas.collectAsState()

    LaunchedEffect(editRecipeId) {
        recipeListViewModel.loadFilters()
        if (editRecipeId != null) {
            val recipe = repository.getRecipeById(editRecipeId)
            recipe?.let {
                title = it.title
                instructions = it.instructions
                imageUri = it.imageUri?.let { path -> Uri.parse(path) }
                existingImagePath = it.imageUri
                selectedCategory = it.category
                selectedArea = it.area
                ingredientList.clear()
                ingredientList.addAll(it.ingredients.split(",").map { it.trim() })
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    val painter = rememberImagePainter(
        data = imageUri ?: "",
        builder = {
            crossfade(true)
            placeholder(R.drawable.recipe_placeholder)
            error(R.drawable.recipe_placeholder)
        }
    )

    fun saveImageToInternalStorage(context: Context, uri: Uri, overwritePath: String? = null): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = if (overwritePath != null) {
                File(overwritePath)
            } else {
                File(context.filesDir, "recipe_image_${UUID.randomUUID()}.jpg")
            }
            val outputStream = FileOutputStream(file, false)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            Log.d("IMG_SAVE", "Zapisano zdjecie do: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e("IMG_SAVE", "Blad zapisu zdjecia", e)
            null
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val savedImagePath = imageUri?.let {
                        saveImageToInternalStorage(
                            context = context,
                            uri = it,
                            overwritePath = existingImagePath
                        )
                    } ?: existingImagePath

                    val recipe = UserRecipe(
                        id = editRecipeId ?: 0,
                        title = title,
                        category = selectedCategory,
                        area = selectedArea,
                        instructions = instructions,
                        ingredients = ingredientList.joinToString(","),
                        imageUri = savedImagePath
                    )
                    if (editRecipeId != null) {
                        viewModel.updateRecipe(recipe) {
                            navController.popBackStack()
                        }
                    } else {
                        viewModel.addRecipe(recipe) {
                            navController.popBackStack()
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Outlined.Save, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        if (editRecipeId != null) "Edit Recipe" else "Add Recipe",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { pickImageLauncher.launch("image/*") }
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "Recipe Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 150.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AddAPhoto,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Tap to add photo", color = Color.White)
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true
                )
            }

            item {
                OutlinedDropdownField(
                    label = "Category",
                    options = categories,
                    selectedOption = selectedCategory,
                    onOptionSelected = { selectedCategory = it }
                )
            }


            item {
                OutlinedDropdownField(
                    label = "Cuisine (Area)",
                    options = areas,
                    selectedOption = selectedArea,
                    onOptionSelected = { selectedArea = it }
                )
            }

            item {
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                )
            }

            item {
                Text("Ingredients", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }

            items(ingredientList.size) { index ->
                OutlinedTextField(
                    value = ingredientList[index],
                    onValueChange = { ingredientList[index] = it },
                    label = { Text("Ingredient ${index + 1}") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true
                )
            }

            item {
                TextButton(onClick = { ingredientList.add("") }) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Add Ingredient")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
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
