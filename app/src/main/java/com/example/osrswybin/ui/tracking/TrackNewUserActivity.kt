package com.example.osrswybin.ui.tracking

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import com.example.osrswybin.database.account.AccountRepository
import com.example.osrswybin.models.Activity
import com.example.osrswybin.models.Hiscores
import com.example.osrswybin.models.OSRSAccount
import com.example.osrswybin.models.Skill
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_track_new_user.*
import kotlinx.android.synthetic.main.skills_overview_first_row.*
import kotlinx.android.synthetic.main.skills_overview_second_row.*
import kotlinx.android.synthetic.main.skills_overview_third_row.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class TrackNewUserActivity : AppCompatActivity() {
    private lateinit var verifyAccount: OSRSAccount
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var accountRepository: AccountRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_new_user)

        // Show back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        btnVerify.setOnClickListener { onVerify(it) }
        btnStartTracking.setOnClickListener { onStartTracking(it) }

        accountRepository =
            AccountRepository(this)
    }

    private fun onVerify(view: View) {
        if(!isValidUsername(etUsername.text.toString())) {
            val toast = Toast.makeText(this, "The username can't be empty or be longer than 30 characters.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }

        Snackbar.make(view, "Trying to get data for ${etUsername.text}", Snackbar.LENGTH_SHORT).show()

        Hiscores.getHiscoresFromUser(etUsername.text.toString(), object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Snackbar.make(view, "Failed to retrieve data for ${etUsername.text}", Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val responseBody = response.body
                    val content = responseBody?.string()

                    val skills: ArrayList<Skill> = Hiscores.getSkillsFromResponse(content)
                    val activities: ArrayList<Activity> = Hiscores.getActivitiesFromResponse(content)

                    verifyAccount = OSRSAccount(0, etUsername.text.toString(), skills, activities, Calendar.getInstance().time)

                    mainScope.launch {
                        tvAttack.text = verifyAccount.getSkillByName(Hiscores.ATTACK).level.toString()
                        tvStrength.text = verifyAccount.getSkillByName(Hiscores.STRENGTH).level.toString()
                        tvDefence.text = verifyAccount.getSkillByName(Hiscores.DEFENCE).level.toString()
                        tvRanged.text = verifyAccount.getSkillByName(Hiscores.RANGED).level.toString()
                        tvPrayer.text = verifyAccount.getSkillByName(Hiscores.PRAYER).level.toString()
                        tvMagic.text = verifyAccount.getSkillByName(Hiscores.MAGIC).level.toString()
                        tvRunecraft.text = verifyAccount.getSkillByName(Hiscores.RUNECRAFTING).level.toString()
                        tvConstruction.text = verifyAccount.getSkillByName(Hiscores.CONSTRUCTION).level.toString()

                        tvHitpoints.text = verifyAccount.getSkillByName(Hiscores.HITPOINTS).level.toString()
                        tvAgility.text = verifyAccount.getSkillByName(Hiscores.AGILITY).level.toString()
                        tvHerblore.text = verifyAccount.getSkillByName(Hiscores.HERBLORE).level.toString()
                        tvThieving.text = verifyAccount.getSkillByName(Hiscores.THIEVING).level.toString()
                        tvCrafting.text = verifyAccount.getSkillByName(Hiscores.CRAFTING).level.toString()
                        tvFletching.text = verifyAccount.getSkillByName(Hiscores.FLETCHING).level.toString()
                        tvSlayer.text = verifyAccount.getSkillByName(Hiscores.SLAYER).level.toString()
                        tvHunter.text = verifyAccount.getSkillByName(Hiscores.HUNTER).level.toString()

                        tvMining.text = verifyAccount.getSkillByName(Hiscores.MINING).level.toString()
                        tvSmithing.text = verifyAccount.getSkillByName(Hiscores.MINING).level.toString()
                        tvFishing.text = verifyAccount.getSkillByName(Hiscores.FISHING).level.toString()
                        tvCooking.text = verifyAccount.getSkillByName(Hiscores.COOKING).level.toString()
                        tvFiremaking.text = verifyAccount.getSkillByName(Hiscores.FIREMAKING).level.toString()
                        tvWoodcutting.text = verifyAccount.getSkillByName(Hiscores.WOODCUTTING).level.toString()
                        tvFarming.text = verifyAccount.getSkillByName(Hiscores.FARMING).level.toString()

                        Snackbar.make(view, "Successfully retrieved data for ${etUsername.text}!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun onStartTracking(view: View) {
        if(!isValidUsername(etUsername.text.toString())) {
            val toast = Toast.makeText(this, "The username can't be empty or be longer than 30 characters.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }

        if(!::verifyAccount.isInitialized) {
            val toast = Toast.makeText(this, "You haven't verified an account yet.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
            return
        }

        mainScope.launch {
            withContext(Dispatchers.IO) {
                accountRepository.insertAccount(verifyAccount)
            }

            Snackbar.make(view, "You are now tracking ${verifyAccount.username}!", Snackbar.LENGTH_SHORT).show()
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
