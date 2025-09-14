package com.example.caturday.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val SELECTED_SOUND = stringPreferencesKey("selected_sound")
    }

    val selectedSound: Flow<Sound> = context.dataStore.data
        .map { preferences ->
            val soundName = preferences[PreferencesKeys.SELECTED_SOUND] ?: Sound.RANDOM.name
            Sound.valueOf(soundName)
        }

    suspend fun saveSelectedSound(sound: Sound) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_SOUND] = sound.name
        }
    }
}
