package com.example.osrswybin.ui.tracking

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import com.example.osrswybin.database.AccountRepository
import com.example.osrswybin.models.Activity
import com.example.osrswybin.models.Hiscores
import com.example.osrswybin.models.OSRSAccount
import com.example.osrswybin.models.Skill
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

        btnVerify.setOnClickListener { onVerify() }
        btnStartTracking.setOnClickListener { onStartTracking() }

        accountRepository = AccountRepository(this)
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
                        tvAttack.text = verifyAccount.getSkillByName("Attack").level.toString()
                        tvStrength.text = verifyAccount.getSkillByName("Strength").level.toString()
                        tvDefence.text = verifyAccount.getSkillByName("Defence").level.toString()
                        tvRanged.text = verifyAccount.getSkillByName("Ranged").level.toString()
                        tvPrayer.text = verifyAccount.getSkillByName("Prayer").level.toString()
                        tvMagic.text = verifyAccount.getSkillByName("Magic").level.toString()
                        tvRunecraft.text = verifyAccount.getSkillByName("Runecrafting").level.toString()
                        tvConstruction.text = verifyAccount.getSkillByName("Construction").level.toString()

                        tvHitpoints.text = verifyAccount.getSkillByName("Hitpoints").level.toString()
                        tvAgility.text = verifyAccount.getSkillByName("Agility").level.toString()
                        tvHerblore.text = verifyAccount.getSkillByName("Herblore").level.toString()
                        tvThieving.text = verifyAccount.getSkillByName("Thieving").level.toString()
                        tvCrafting.text = verifyAccount.getSkillByName("Crafting").level.toString()
                        tvFletching.text = verifyAccount.getSkillByName("Fletching").level.toString()
                        tvSlayer.text = verifyAccount.getSkillByName("Slayer").level.toString()
                        tvHunter.text = verifyAccount.getSkillByName("Hunter").level.toString()

                        tvMining.text = verifyAccount.getSkillByName("Mining").level.toString()
                        tvSmithing.text = verifyAccount.getSkillByName("Smithing").level.toString()
                        tvFishing.text = verifyAccount.getSkillByName("Fishing").level.toString()
                        tvCooking.text = verifyAccount.getSkillByName("Cooking").level.toString()
                        tvFiremaking.text = verifyAccount.getSkillByName("Firemaking").level.toString()
                        tvWoodcutting.text = verifyAccount.getSkillByName("Woodcutting").level.toString()
                        tvFarming.text = verifyAccount.getSkillByName("Farming").level.toString()
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

            val toast = Toast.makeText(baseContext, "You are now tracking ${verifyAccount.username}!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 16)
            toast.show()
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
