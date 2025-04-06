package com.nomnomapp.nomnom.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.nomnomapp.nomnom.data.local.AppDatabase
import com.nomnomapp.nomnom.data.local.entity.UserRecipe
import com.nomnomapp.nomnom.data.repository.LocalRecipeRepository
import com.nomnomapp.nomnom.viewmodel.AddRecipeViewModel
import androidx.room.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nomnom.db"
        ).build()
    }
    val viewModel = remember { AddRecipeViewModel(LocalRecipeRepository(db.userRecipeDao())) }

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val recipe = UserRecipe(
                        title = title,
                        category = category,
                        area = area,
                        instructions = instructions,
                        ingredients = ingredients,
                        imageUri = imageUri?.toString() // konwersja do String
                    )
                    viewModel.addRecipe(recipe) {
                        navController.popBackStack()
                    }
                }
            ) {
                Icon(Icons.Outlined.Save, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "Add Recipe",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            val defaultImage = R.drawable.recipe_placeholder
            val painter = rememberImagePainter(
                data = imageUri ?: "",
                builder = {
                    crossfade(true)
                    error(defaultImage)
                    placeholder(defaultImage)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
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
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                }

                item {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        label = { Text("Cuisine / Area") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instructions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        label = { Text("Ingredients (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
