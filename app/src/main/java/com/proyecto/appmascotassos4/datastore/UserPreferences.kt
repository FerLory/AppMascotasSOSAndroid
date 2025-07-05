package com.proyecto.appmascotassos4.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

private const val s = "registro_completo"

class UserPreferences(private val context: Context) {

    companion object {
        private val REGISTRO_COMPLETO = booleanPreferencesKey(s)
    }

    suspend fun guardarRegistroCompleto(completo: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REGISTRO_COMPLETO] = completo
        }
    }

    fun obtenerRegistroCompleto(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[REGISTRO_COMPLETO] ?: false
        }
    }
}
