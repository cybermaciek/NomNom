package com.nomnomapp.nomnom.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Recipe") })
        }
    ) { padding ->
        Text(
            text = "Form to add recipe coming soon...",
            modifier = Modifier.padding(padding)
        )
    }
}

@Preview
@Composable
fun AddRecipeScreenPreview() {
    AddRecipeScreen()
}
