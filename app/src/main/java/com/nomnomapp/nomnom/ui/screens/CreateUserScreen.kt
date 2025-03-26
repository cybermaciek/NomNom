package com.nomnomapp.nomnom.ui.screens


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nomnomapp.nomnom.ui.theme.NomNomTheme


@Composable
fun CreateUserScreen(
    onNavigateToHome: () -> Unit
){
    Scaffold { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                    .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ){
                Icon(
                    imageVector = Icons.Outlined.AddAPhoto,
                    contentDescription = "Add Photo Icon",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(60.dp)
                )
            }

            var yourName by remember { mutableStateOf(TextFieldValue("")) }
            OutlinedTextField(
                value = yourName,
                onValueChange = { yourName = it },
                label = { Text(
                    text = "Your name",
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp
                ) },
                placeholder = { Text(text = "Enter your name") },
                shape = RoundedCornerShape(15.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Create,
                    contentDescription = "emailIcon",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 10.dp)
                        .size(36.dp)

                    ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 54.dp, bottom = 54.dp)
                    .height(60.dp)

            )

            Button(
                onClick = { onNavigateToHome() }, // Navigate to HomeScreen
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(bottom = 240.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAdd,
                        contentDescription = "Create Profile icon",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Create Profile",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
@Composable
fun CreateUser_LightmodePreview() {
    NomNomTheme {
        CreateUserScreen(
            onNavigateToHome = {} // Provide an empty lambda
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun CreateUser_DarkmodePreview() {
    NomNomTheme {
        CreateUserScreen(
            onNavigateToHome = {} // Provide an empty lambda
        )
    }
}

