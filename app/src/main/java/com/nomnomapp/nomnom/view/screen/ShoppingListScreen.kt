package com.nomnomapp.nomnom.view.screen

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nomnomapp.nomnom.ui.theme.NomNomTheme

class ShoppingListScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NomNomTheme {
                ShoppingListScreenPreview()
            }
        }
    }


    @Composable
    fun ShoppingListScreenPreview() {
        //TYMCZASOWA DEFINCJA LISTY DO ZAKUPU
        val toBuy = listOf("Apple", "Orange juice", "Ketchup")
        val recent = listOf("Potatoes", "Chicken breast", "Bread", "Cheese", "Eggs")

        ShoppingListScreenView(toBuyItems = toBuy, recentItems = recent)
    }


    @Composable
    fun ShoppingListScreenView(
        toBuyItems: List<String>,
        recentItems: List<String>
    ) {

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
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                Text("Shopping List", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pole dodawania
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Add product...", color = Color.Gray, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add icon", tint = MaterialTheme.colorScheme.onBackground)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Do kupienia
            if (toBuyItems.isNotEmpty()) {
                Text("To buy", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    toBuyItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {})
                                .background(Color(0xFF00796B), shape = RoundedCornerShape(15.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, color = Color.White)
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove", tint = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ostatnie zakupy
            if (recentItems.isNotEmpty()) {
                Text("Shopping history", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recentItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {})
                                .background(Color.Gray, shape = RoundedCornerShape(15.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, color = Color.White)
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
    @Composable
    fun LightmodePreview() {
        NomNomTheme {
            ShoppingListScreenPreview()
        }
    }

    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
    @Composable
    fun DarkmodePreview() {
        NomNomTheme {
            ShoppingListScreenPreview()
        }
    }


}

