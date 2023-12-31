package com.example.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.core.navigation.NavigationScreens

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                navController.navigate(NavigationScreens.GAME_SCREEN.route) {
                    popUpTo(NavigationScreens.HOMET_SCREEN.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) {
            Text(text = context.getString(R.string.home_game_start_button))
        }
    }
}
