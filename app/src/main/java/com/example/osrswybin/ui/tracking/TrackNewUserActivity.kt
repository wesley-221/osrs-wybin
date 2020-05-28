package com.example.osrswybin.ui.tracking

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import kotlinx.android.synthetic.main.activity_track_new_user.*

class TrackNewUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_new_user)

        // Show back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        btnVerify.setOnClickListener { onVerify() }
        btnStartTracking.setOnClickListener { onStartTracking() }
    }

    fun onVerify() {
        if(!isValidUsername(etUsername.text.toString())) {
            val toast = Toast.makeText(this, "The username can't be empty or be longer than 30 characters.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }
    }

    fun onStartTracking() {
        if(!isValidUsername(etUsername.text.toString())) {
            val toast = Toast.makeText(this, "The username can't be empty or be longer than 30 characters.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }
    }

    private fun isValidUsername(username: String): Boolean {
        return username.isNotBlank() && username.length <= 30;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}
