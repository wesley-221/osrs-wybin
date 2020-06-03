package com.example.osrswybin.models

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class Authentication {
    companion object {
        val API_URL = "https://wybin.xyz:8080"
        val AUTHENTICATION_URL = "$API_URL/login"
        val REGISTER_URL = "$API_URL/register"

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
    }
}