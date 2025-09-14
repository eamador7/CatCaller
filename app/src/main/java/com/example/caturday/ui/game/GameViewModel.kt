package com.example.caturday.ui.game

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caturday.data.Sound
import com.example.caturday.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameState(
    val availableSounds: List<Sound> = Sound.values().toList(),
    val selectedSound: Sound = Sound.RANDOM
)

class GameViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    init {
        userPreferencesRepository.selectedSound
            .onEach { selectedSound ->
                _uiState.update { it.copy(selectedSound = selectedSound) }
            }
            .launchIn(viewModelScope)
    }

    fun selectSound(sound: Sound) {
        viewModelScope.launch {
            userPreferencesRepository.saveSelectedSound(sound)
        }
    }

    fun playSound(context: Context) {
        viewModelScope.launch {
            val soundToPlay = if (uiState.value.selectedSound == Sound.RANDOM) {
                Sound.values().filter { it != Sound.RANDOM }.random()
            } else {
                uiState.value.selectedSound
            }

            soundToPlay.fileName?.let { fileName ->
                val resId = context.resources.getIdentifier(fileName, "raw", context.packageName)
                if (resId != 0) {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(context, resId).apply {
                        setOnCompletionListener { it.release() }
                        start()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
    }
}

class GameViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
