package com.nomnomapp.nomnom.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.data.local.DatabaseProvider
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.viewmodel.AddRecipeViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

//zdj zapisywane w /data/data/com.nomnomapp.nomnom/files/recipe_image_<UUID>.jpg


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

    var title by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    val ingredientList = remember { mutableStateListOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(editRecipeId) {
        if (editRecipeId != null) {
            val recipe = repository.getRecipeById(editRecipeId)
            recipe?.let {
                title = it.title
                instructions = it.instructions
                imageUri = it.imageUri?.let { uriStr -> Uri.parse(uriStr) }
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

    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val fileName = "recipe_image_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val savedImagePath = imageUri?.let { saveImageToInternalStorage(context, it) }
                    val recipe = UserRecipe(
                        id = editRecipeId ?: 0,
                        title = title,
                        category = "",
                        area = "",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
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