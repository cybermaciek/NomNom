package com.nomnomapp.nomnom.ui.screens

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.ui.theme.NomNomTheme



@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val colorBackground = MaterialTheme.colorScheme.background
    val colorOnBackground = MaterialTheme.colorScheme.onBackground
    val colorOrange = MaterialTheme.colorScheme.primary
    val colorGreen = MaterialTheme.colorScheme.secondary
    val colorBlue = MaterialTheme.colorScheme.tertiary

    val themeOptions = listOf("Light", "Dark")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(themeOptions[1]) }

    Scaffold() { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Button(
                onClick = {/* TODO */},
                colors = ButtonDefaults.buttonColors(containerColor = colorOrange) ,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(top = 36.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "Edit",
                        tint = colorBackground,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Change name",
                        color = colorBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Button(
                onClick = {/* TODO */},
                colors = ButtonDefaults.buttonColors(containerColor = colorOrange) ,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "Edit",
                        tint = colorBackground,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Change photo",
                        color = colorBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = colorOnBackground,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Choose theme",
                    color = colorOnBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )

                themeOptions.forEach { text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            color = colorOnBackground,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = colorOnBackground,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Button(
                onClick = {/* TODO */},
                colors = ButtonDefaults.buttonColors(containerColor = colorOrange) ,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = colorBackground,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Share app",
                        color = colorBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Button(
                onClick = {/* TODO */},
                colors = ButtonDefaults.buttonColors(containerColor = colorOrange) ,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_delete),
                        contentDescription = "Restore defaults icon",
                        tint = colorBackground,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Restore defaults",
                        color = colorBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = colorOnBackground,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Text(
                    text = "About",
                    color = colorOnBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "v1.0.0",
                    color = colorOnBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )

                Text(
                    text = "Made by Maciej Chitrosz & Adrian Jargiło",
                    color = colorOnBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )

                Text(
                    text = "2025",
                    color = colorOnBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
@Composable
fun Settings_LightmodePreview() {
    NomNomTheme {
        SettingsScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun Settings_DarkmodePreview() {
    NomNomTheme{
        SettingsScreen()
    }
}

