package com.ipt2025.project_dam.data

import android.content.Context

// manage the auth token using shared preferences (for automatic)
class TokenManager(context: Context) {

    // access the app shared preferences storage
    private val prefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    // saves the api token to local storage so for later access on next app startup
    fun saveToken(token: String) {
        prefs.edit().putString("api_token", token).apply()
    }

    // tries to get the saved token, returns null if it's not there
    fun loadToken(): String? {
        val result = prefs.getString("api_token", null) ?: return null
        return result
    }

    // wipes the token. Used when the user logs out
    fun clearToken() {
        prefs.edit().remove("api_token").apply()
    }
}