package com.nomnomapp.nomnom.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.UserDataManager

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null  // Make nullable for previews
) {
    SettingsScreenView(
        modifier = modifier,
        onBackClick = { navController?.popBackStack() },
        onEditProfileClick = {
            navController?.navigate(Routes.CREATE_USER.route) {
                // Clear the back stack up to HOME when navigating to CREATE_USER
                popUpTo(Routes.HOME.route) { inclusive = false }
            }
        }
    )
}

@Composable
fun SettingsScreenView(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val themeOptions = listOf("Light", "Dark", "System")
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(UserDataManager.currentTheme.value)
    }
    Scaffold(modifier = modifier) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
                    text = "Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEditProfileClick() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) ,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
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
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Change photo / name",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
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
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )

                Column {
                    themeOptions.forEach { text ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                        val theme = when (text) {
                                            "Light" -> UserDataManager.AppTheme.LIGHT
                                            "Dark" -> UserDataManager.AppTheme.DARK
                                            else -> UserDataManager.AppTheme.SYSTEM
                                        }
                                        UserDataManager.saveThemePreference(context, theme)
                                    }
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null // null because we handle selection in parent Row
                            )
                            Text(
                                text = text,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Button(
                onClick = {
                    // Replace with your actual app link
                    val appLink = "https://github.com/cybermaciek/NomNom"
                    copyToClipboard(context, appLink)
                    showToast(context, "App link copied to clipboard")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) ,
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
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Share app",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Button(
                onClick = {/* TODO */},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) ,
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
                        contentDescription = "Restore data",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Restore data",
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
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
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "v1.0.0",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )

                Text(
                    text = "Made by Maciej Chitrosz & Adrian Jargiło",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )

                Text(
                    text = "2025",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp
                )
            }
        }
    }
}private fun copyToClipboard(context: Context, text: String) {
    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("App download link", text)
    clipboard?.setPrimaryClip(clip)
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
@Composable
fun Settings_LightmodePreview() {
    NomNomTheme {
        SettingsScreenView(
            onBackClick = {},
            onEditProfileClick = {},
            context = LocalContext.current
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun Settings_DarkmodePreview() {
    NomNomTheme {
        SettingsScreenView(
            onBackClick = {},
            onEditProfileClick = {},
            context = LocalContext.current
        )
    }
}
