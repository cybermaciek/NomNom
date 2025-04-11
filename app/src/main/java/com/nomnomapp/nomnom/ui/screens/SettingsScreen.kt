package com.nomnomapp.nomnom.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nomnomapp.nomnom.R
import com.nomnomapp.nomnom.model.VideoPlayer
import com.nomnomapp.nomnom.ui.navigation.Routes
import com.nomnomapp.nomnom.ui.theme.NomNomTheme
import com.nomnomapp.nomnom.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val context = LocalContext.current
    val videoUri = remember {
        Uri.parse("android.resource://${context.packageName}/${R.raw.video}")
    }
    val viewModel: SettingsViewModel = viewModel()
    val isScreenVisible by rememberNavControllerVisibility(navController, Routes.SETTINGS)

    SettingsScreenView(
        modifier = modifier,
        onBackClick = { navController?.popBackStack() },
        onEditProfileClick = {
            navController?.navigate(Routes.CREATE_USER.route) {
                popUpTo(Routes.HOME.route) { inclusive = false }
            }
        },
        videoUri = videoUri,
        isScreenVisible = isScreenVisible,
        viewModel = viewModel
    )
}

@Composable
fun SettingsScreenView(
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    videoUri: Uri,
    isScreenVisible: Boolean,
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    val themeOptions = listOf("Light", "Dark", "System")

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
                        tint = Color.White,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Change photo / name",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Button(
                onClick = {
                    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                    clipboard?.setPrimaryClip(
                        ClipData.newPlainText("", "https://github.com/cybermaciek/NomNom")
                    )
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
                        tint = Color.White,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Share app",
                        color = Color.White,
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
                                    selected = (text == viewModel.selectedTheme),
                                    onClick = {
                                        viewModel.updateTheme(text, context)
                                    }
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (text == viewModel.selectedTheme),
                                onClick = null
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

            Spacer(modifier = Modifier.height(8.dp))

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
                    text = "v1.0.0\nMade by Maciej Chitrosz & Adrian Jargiło\n2025",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            VideoPlayer(
                modifier = Modifier
                    .width(300.dp)
                    .padding(8.dp),
                videoUri = videoUri,
                cornerRadius = 48f,
                isVisible = isScreenVisible
            )
        }
    }
}

@Composable
fun rememberNavControllerVisibility(
    navController: NavController?,
    targetRoute: Routes
): State<Boolean> {
    val isVisible = remember { mutableStateOf(true) }

    DisposableEffect(navController) {
        if (navController == null) {
            return@DisposableEffect onDispose {}
        }

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            isVisible.value = destination.route?.substringBefore('?') == targetRoute.route
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    return isVisible
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme")
@Composable
fun Settings_LightmodePreview() {
    val dummyVideoUri = remember {
        Uri.parse("android.resource://com.nomnomapp.nomnom/${R.raw.video}")
    }

    NomNomTheme {
        SettingsScreenView(
            onBackClick = {},
            onEditProfileClick = {},
            videoUri = dummyVideoUri,
            isScreenVisible = true,
            viewModel = SettingsViewModel(),
            context = LocalContext.current
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme")
@Composable
fun Settings_DarkmodePreview() {
    val dummyVideoUri = remember {
        Uri.parse("android.resource://com.nomnomapp.nomnom/${R.raw.video}")
    }

    NomNomTheme {
        SettingsScreenView(
            onBackClick = {},
            onEditProfileClick = {},
            videoUri = dummyVideoUri,
            isScreenVisible = true,
            viewModel = SettingsViewModel(),
            context = LocalContext.current
        )
    }
}