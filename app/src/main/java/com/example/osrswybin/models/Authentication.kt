package com.example.osrswybin.models

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class Authentication {
    companion object {
        private const val API_URL = "https://wybin.xyz:8080"
        private const val AUTHENTICATION_URL = "$API_URL/login"
        private const val REGISTER_URL = "$API_URL/register"
        private const val TRACK_USER_UPDATE_URL = "$API_URL/osrs/update"
        private const val TRACK_USER_GET_URL = "$API_URL/osrs/get"

        fun login(username: String, password: String, callback: Callback) {
            val requestBody: JSONObject = JSONObject()
                .put("username", username)
                .put("password", password)

            val client = OkHttpClient()
            val request = Request
                .Builder()
                .url(AUTHENTICATION_URL)
                .post(requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).enqueue(callback)
        }

        fun register(username: String, password: String, passwordConfirm: String, callback: Callback) {
            val requestBody: JSONObject = JSONObject()
                .put("username", username)
                .put("password", password)
                .put("passwordConfirm", passwordConfirm)

            val client = OkHttpClient()
            val request = Request
                .Builder()
                .url(REGISTER_URL)
                .post(requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).enqueue(callback)
        }

        fun saveTrackedUsers(trackedUsers: List<OSRSAccount>, header: String, callback: Callback) {
             val trackedUsernames: ArrayList<String> = arrayListOf()

            for (osrsAccount in trackedUsers) {
                osrsAccount.username?.let { trackedUsernames.add(it) }
            }

            val client = OkHttpClient()
            val requestBody: JSONObject = JSONObject()
                .put("users", trackedUsernames)

            val request = Request
                .Builder()
                .url(TRACK_USER_UPDATE_URL)
                .header("Authorization", header)
                .post(requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).enqueue(callback)
        }

        fun getTrackedUsers(header: String, callback : Callback) {
            val client = OkHttpClient()
            val request = Request
                .Builder()
                .url(TRACK_USER_GET_URL)
                .header("Authorization", header)
                .build()

            client.newCall(request).enqueue(callback)
        }
    }
}