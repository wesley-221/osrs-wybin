package com.example.osrswybin.ui.tracking

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import com.example.osrswybin.models.Activity
import com.example.osrswybin.models.Hiscores
import com.example.osrswybin.models.OSRSAccount
import com.example.osrswybin.models.Skill
import kotlinx.android.synthetic.main.activity_track_new_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TrackNewUserActivity : AppCompatActivity() {
    private lateinit var verifyAccount: OSRSAccount
    private val mainScope = CoroutineScope(Dispatchers.Main)

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

        Hiscores.getHiscoresFromUser(etUsername.text.toString(), object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseBody = response.body
                    val content = responseBody?.string()

                    val skills: ArrayList<Skill> = Hiscores.getSkillsFromResponse(content)
                    val activities: ArrayList<Activity> = Hiscores.getActivitiesFromResponse(content)

                    verifyAccount = OSRSAccount(0, etUsername.text.toString(), skills, activities)

                    mainScope.launch {
                        tvUsername.text = getString(R.string.verify_username, verifyAccount.username)
                        tvTotalLevel.text = getString(  R.string.verify_level,
                                                        verifyAccount.getSkillByName("Overall").level.toString(),
                                                        NumberFormat.getNumberInstance(Locale.UK).format(verifyAccount.getSkillByName("Overall").experience))
                    }
                }
            }
        })
    }

    fun onStartTracking() {
        if(!isValidUsername(etUsername.text.toString())) {
            val toast = Toast.makeText(this, "The username can't be empty or be longer than 30 characters.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }

        TODO("implement")
    }

    private fun isValidUsername(username: String): Boolean {
        return username.isNotBlank() && username.length <= 30;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}
