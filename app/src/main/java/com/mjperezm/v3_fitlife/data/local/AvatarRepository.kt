package com.mjperezm.v3_fitlife.data.local


import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.avatarDataStore by preferencesDataStore(name = "avatar_prefs")
class AvatarRepository (private val context: Context){
    companion object {
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    }

    fun getAvatarUri(): Flow<Uri?> {
        return context.avatarDataStore.data.map { prefs ->
            prefs[AVATAR_URI_KEY]?.let { Uri.parse(it) }
        }
    }

    suspend fun saveAvatarUri(uri: Uri?) {
        if (uri != null) {
            context.avatarDataStore.edit { prefs ->
                prefs[AVATAR_URI_KEY] = uri.toString()
            }
        } else {
            clearAvatarUri()
        }
    }

    suspend fun clearAvatarUri() {
        context.avatarDataStore.edit { prefs ->
            prefs.remove(AVATAR_URI_KEY)
        }
    }
}