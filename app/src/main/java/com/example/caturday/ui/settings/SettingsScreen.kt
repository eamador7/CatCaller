package com.example.caturday.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.caturday.R
import com.example.caturday.data.Sound
import com.example.caturday.ui.game.GameState
import com.example.caturday.ui.theme.CaturdayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    gameState: GameState,
    onSoundSelected: (Sound) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(gameState.availableSounds) { sound ->
                SoundSelectionRow(
                    sound = sound,
                    isSelected = sound == gameState.selectedSound,
                    onSoundSelected = { onSoundSelected(sound) }
                )
            }
        }
    }
}

@Composable
fun SoundSelectionRow(
    sound: Sound,
    isSelected: Boolean,
    onSoundSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSoundSelected)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSoundSelected
        )
        Text(
            text = sound.displayName,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    CaturdayTheme {
        SettingsScreen(
            gameState = GameState(),
            onSoundSelected = {},
            onBackClick = {}
        )
    }
}
