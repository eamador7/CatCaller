package com.example.caturday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caturday.data.UserPreferencesRepository
import com.example.caturday.ui.game.GameViewModel
import com.example.caturday.ui.game.GameViewModelFactory
import com.example.caturday.ui.main.MainScreen
import com.example.caturday.ui.settings.SettingsScreen
import com.example.caturday.ui.theme.CaturdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferencesRepository = UserPreferencesRepository(this)
        setContent {
            CaturdayTheme {
                val navController = rememberNavController()
                val viewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory(userPreferencesRepository)
                )
                val gameState by viewModel.uiState.collectAsState()
                val context = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(
                                gameState = gameState,
                                onPlaySoundClick = { viewModel.playSound(context) },
                                onSettingsClick = { navController.navigate("settings") }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                gameState = gameState,
                                onSoundSelected = viewModel::selectSound,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
