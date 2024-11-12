package com.ilgusu.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ilgusu.domain.model.TokenPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenLocalDataSource {

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
                preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTokens(): Flow<TokenPreferences> {
        return dataStore.data.map { preferences ->
            TokenPreferences(
                accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: "",
                refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN] ?: "",
            )
        }
    }

    override suspend fun clearTokens(): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences.clear()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}