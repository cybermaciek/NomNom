package com.nomnomapp.nomnom.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.ShoppingListViewModel

@Composable
fun ShoppingListScreen(
    navController: NavController? = null
) {
    val context = LocalContext.current
    val viewModel: ShoppingListViewModel = remember {
        ShoppingListViewModel(context)
    }

    val toBuyItems by viewModel.itemsInCart.collectAsState(initial = emptyList())
    val recentItems by viewModel.recentItems.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()

    ShoppingListScreenView(
        toBuyItems = toBuyItems.map { it.name },
        recentItems = recentItems.map { it.name },
        searchQuery = searchQuery,
        onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
        onAddItem = { viewModel.addOrRemoveItem(it) },
        onRemoveItem = { viewModel.moveToRecent(it) },
        onBackClick = { navController?.popBackStack() },
        onSettingsClick = { navController?.navigate(Routes.SETTINGS.route) },
        onDeleteItem = { viewModel.deleteItem(it) }
    )
}

@Composable
fun ShoppingListScreenView(
    toBuyItems: List<String>,
    recentItems: List<String>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onAddItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDeleteItem: (String) -> Unit
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    Text(
                        text = "Shopping List",
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
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
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
                                if (searchQuery.isBlank()) {
                                    Text(
                                        text = "Add...",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        maxLines = 1,
                                    )
                                }
                                innerTextField()
                            }

                            if (searchQuery.isNotBlank()) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .clickable {
                                            onAddItem(searchQuery)
                                            onSearchQueryChanged("")
                                        }
                                )
                            }
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
            }

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (toBuyItems.isNotEmpty()) {
                    item {
                        Column {
                            Text(
                                "To buy",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    items(toBuyItems) { item ->
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Delete item") },
                                text = { Text("Are you sure you want to delete \"$item\"?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onDeleteItem(item)
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDeleteDialog = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(item) {
                                    detectTapGestures(
                                        onTap = { onRemoveItem(item) },
                                        onLongPress = { showDeleteDialog = true }
                                    )
                                }
                                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(15.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item, color = Color.White)
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remove",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (recentItems.isNotEmpty()) {
                    item {
                        Column {
                            Text(
                                "Shopping history",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    items(recentItems) { item ->
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Delete item") },
                                text = { Text("Are you sure you want to delete \"$item\"?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            onDeleteItem(item)
                                            showDeleteDialog = false
                                        }
                                    ) { Text("Delete") }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDeleteDialog = false }
                                    ) { Text("Cancel") }
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(item) {
                                    detectTapGestures(
                                        onTap = { onAddItem(item) },
                                        onLongPress = { showDeleteDialog = true }
                                    )
                                }
                                .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(15.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item, color = MaterialTheme.colorScheme.background)
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListScreenPreview() {
    NomNomTheme {
        ShoppingListScreenView(
            toBuyItems = listOf("Apples", "Milk", "Bread"),
            recentItems = listOf("Eggs", "Cheese", "Pasta"),
            searchQuery = "",
            onSearchQueryChanged = {},
            onAddItem = {},
            onRemoveItem = {},
            onBackClick = {},
            onSettingsClick = {},
            onDeleteItem = {}
        )
    }
}
