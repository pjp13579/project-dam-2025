package com.ipt2025.project_dam.data

import android.content.Context

class TokenManager(context: Context) {

    private val prefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("api_token", token).apply()
    }

    fun loadToken(): String? {
        val result = prefs.getString("api_token", null) ?: return null
        return result
    }

    fun clearToken() {
        prefs.edit().remove("api_token").apply()
    }
}