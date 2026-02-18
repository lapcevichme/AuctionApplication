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
        private val EXPIRES_AT_KEY = stringPreferencesKey("expires_at")
    }

    suspend fun getTokens(): AuthTokens {
        return context.dataStore.data.map { preferences ->
            AuthTokens(
                accessToken = preferences[ACCESS_TOKEN_KEY]?.let { cryptoManager.decrypt(it) },
                refreshToken = preferences[REFRESH_TOKEN_KEY]?.let { cryptoManager.decrypt(it) },
                expiresAt = preferences[EXPIRES_AT_KEY]?.toLongOrNull()
            )
        }.first()
    }

    suspend fun saveTokens(tokens: AuthTokens) {
        val encryptedAccess = tokens.accessToken?.let { cryptoManager.encrypt(it) }
        val encryptedRefresh = tokens.refreshToken?.let { cryptoManager.encrypt(it) }

        context.dataStore.edit { preferences ->
            if (encryptedAccess != null) preferences[ACCESS_TOKEN_KEY] = encryptedAccess
            if (encryptedRefresh != null) preferences[REFRESH_TOKEN_KEY] = encryptedRefresh
            preferences[EXPIRES_AT_KEY] = tokens.expiresAt.toString()
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}