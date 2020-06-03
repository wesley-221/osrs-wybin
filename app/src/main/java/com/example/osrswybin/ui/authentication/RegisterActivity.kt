package com.example.osrswybin.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import com.example.osrswybin.models.Authentication
import com.example.osrswybin.models.MessageHelper
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)

        // Show back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        btnRegister.setOnClickListener {
            if(tbPassword.text.toString() != tbPasswordConfirmation.text.toString()) {
                Toast.makeText(this.applicationContext, "The passwords you have entered do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(tbPassword.text.isBlank() || tbPasswordConfirmation.text.isBlank()) {
                Toast.makeText(this.applicationContext, "You have to enter a password in order to register.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Authentication.register(tbUsername.text.toString(), tbPassword.text.toString(), tbPasswordConfirmation.text.toString(), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful) {
                        mainScope.launch {
                            Toast.makeText(applicationContext,"Successfully registered your account with the username ${tbUsername.text}.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        val responseBody = response.body
                        val content = Gson().fromJson(responseBody?.string(), MessageHelper::class.java)
                        Toast.makeText(applicationContext, content.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}
