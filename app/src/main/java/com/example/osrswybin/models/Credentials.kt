package com.example.osrswybin.models

import android.content.Context
import android.util.Log

class Credentials {
    companion object {
        private val PREFERENCE_NAME = "authentication"
        private val ACCESS_TOKEN = "access_token"
        private val USERNAME = "username"

        fun saveCredentials(context: Context, authenticationToken: String?, username: String?) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            sharedPreferences.edit().putString(ACCESS_TOKEN, authenticationToken).apply()
            sharedPreferences.edit().putString(USERNAME, username).apply()
        }

        fun getAccessToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            return sharedPreferences.getString(ACCESS_TOKEN, null)
        }

        fun getUsername(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            return sharedPreferences.getString(USERNAME, null)
        }

        fun logOut(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
        }
    }
}