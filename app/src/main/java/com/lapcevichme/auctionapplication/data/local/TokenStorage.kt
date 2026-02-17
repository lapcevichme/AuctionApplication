package com.lapcevichme.auctionapplication.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lapcevichme.auctionapplication.domain.model.AuthTokens
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class TokenStorage(private val context: Context) {

    private val cryptoManager = CryptoManager()

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun getTokens(): AuthTokens {
        return context.dataStore.data.map { preferences ->
            AuthTokens(
                accessToken = preferences[ACCESS_TOKEN_KEY]?.let { cryptoManager.decrypt(it) },
                refreshToken = preferences[REFRESH_TOKEN_KEY]?.let { cryptoManager.decrypt(it) }
            )
        }.first()
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        val encryptedAccess = cryptoManager.encrypt(accessToken)
        val encryptedRefresh = cryptoManager.encrypt(refreshToken)

        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encryptedAccess
            preferences[REFRESH_TOKEN_KEY] = encryptedRefresh
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}