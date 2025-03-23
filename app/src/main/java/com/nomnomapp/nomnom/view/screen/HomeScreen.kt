package com.nomnomapp.nomnom.view.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.view.ui.theme.*


class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NomNomTheme {
                menuPreviewFunction()
            }
        }
    }

    @Composable
    fun menuPreviewFunction(modifier: Modifier = Modifier) {
        val name: String = "Maciuś"
        val colorBackground = MaterialTheme.colorScheme.background
        val colorOnBackground = MaterialTheme.colorScheme.onBackground
        val colorOrange = MaterialTheme.colorScheme.primary
        val colorGreen = MaterialTheme.colorScheme.secondary
        val colorBlue = MaterialTheme.colorScheme.tertiary

        Scaffold() { contentPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.sample_avatar),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(5.dp, colorOrange, CircleShape)
                )

                Row(modifier = Modifier.padding(top = 10.dp)) {
                    Text(buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorOnBackground)) {
                            append("Witaj, ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = colorOrange)) {
                            append(name)
                        }
                        withStyle(style = SpanStyle(color = colorOnBackground)) {
                            append("!")
                        }
                    },
                        fontSize = 36.sp
                    )
                }


                //TODO: Tymczasowo dodane obsługa przycisku następnie przeniesc ją do ViewModel
                val context = LocalContext.current
                Button(
                    onClick = {context.startActivity(Intent(context, ShoppingListScreen::class.java))},
                    colors = ButtonDefaults.buttonColors(containerColor = colorGreen) ,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(
                            top = 40.dp,
                            bottom = 8.dp,
                        )
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_shopping_cart),
                            contentDescription = "Shopping Cart Icon",
                            tint = colorBackground,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(36.dp)

                        )
                        Text(
                            text = "Lista zakupów",
                            color = colorBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                }

                //TODO: Tymczasowo dodane obsługa przycisku następnie przeniesc ją do ViewModel
                Button(
                    onClick = {context.startActivity(Intent(context, RecipeListScreen::class.java))},
                    colors = ButtonDefaults.buttonColors(containerColor = colorBlue) ,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_chefs_hat),
                            contentDescription = "Chef's Hat Icon",
                            tint = colorBackground,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(36.dp)

                        )
                        Text(
                            text = "Przepisy",
                            color = colorBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                //TODO: Tymczasowo dodane obsługa przycisku następnie przeniesc ją do ViewModel
                Button(
                    onClick = {context.startActivity(Intent(context, SettingsScreen::class.java))},
                    colors = ButtonDefaults.buttonColors(containerColor = colorOnBackground) ,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .padding(top = 150.dp, bottom = 30.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Ustawienia",
                            tint = colorBackground,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(36.dp)
                        )
                        Text(
                            text = "Ustawienia",
                            color = colorBackground,
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
    fun LightmodePreview() {
        NomNomTheme {
            menuPreviewFunction()
        }
    }

    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
    @Composable
    fun DarkmodePreview() {
        NomNomTheme {
            menuPreviewFunction()
        }
    }
}


