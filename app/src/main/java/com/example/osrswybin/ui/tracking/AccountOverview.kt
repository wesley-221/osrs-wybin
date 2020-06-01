package com.example.osrswybin.ui.tracking

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.osrswybin.R
import com.example.osrswybin.database.AccountRepository
import com.example.osrswybin.models.Activity
import com.example.osrswybin.models.Hiscores
import com.example.osrswybin.models.OSRSAccount
import com.example.osrswybin.models.Skill
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_account_overview.*
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
import java.text.SimpleDateFormat
import java.util.*

class AccountOverview : AppCompatActivity() {
    private var accountId: Int = -1

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var accountRepository: AccountRepository
    private lateinit var osrsAccount: OSRSAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_overview)

        // Show back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        accountId = intent.getIntExtra("accountId", -1)
        accountRepository = AccountRepository(this)

        btnUpdate.setOnClickListener { updateAccount(it) }


        mainScope.launch {
            withContext(Dispatchers.IO) {
                osrsAccount = accountRepository.getAccountById(accountId)
            }

            actionBar?.title = osrsAccount.username
            tvLastUpdate.text = getString(R.string.last_update, timeFormat().format(osrsAccount.lastUpdate))

            tvAttack.text = osrsAccount.getSkillByName("Attack").level.toString()
            tvStrength.text = osrsAccount.getSkillByName("Strength").level.toString()
            tvDefence.text = osrsAccount.getSkillByName("Defence").level.toString()
            tvRanged.text = osrsAccount.getSkillByName("Ranged").level.toString()
            tvPrayer.text = osrsAccount.getSkillByName("Prayer").level.toString()
            tvMagic.text = osrsAccount.getSkillByName("Magic").level.toString()
            tvRunecraft.text = osrsAccount.getSkillByName("Runecrafting").level.toString()
            tvConstruction.text = osrsAccount.getSkillByName("Construction").level.toString()

            tvHitpoints.text = osrsAccount.getSkillByName("Hitpoints").level.toString()
            tvAgility.text = osrsAccount.getSkillByName("Agility").level.toString()
            tvHerblore.text = osrsAccount.getSkillByName("Herblore").level.toString()
            tvThieving.text = osrsAccount.getSkillByName("Thieving").level.toString()
            tvCrafting.text = osrsAccount.getSkillByName("Crafting").level.toString()
            tvFletching.text = osrsAccount.getSkillByName("Fletching").level.toString()
            tvSlayer.text = osrsAccount.getSkillByName("Slayer").level.toString()
            tvHunter.text = osrsAccount.getSkillByName("Hunter").level.toString()

            tvMining.text = osrsAccount.getSkillByName("Mining").level.toString()
            tvSmithing.text = osrsAccount.getSkillByName("Smithing").level.toString()
            tvFishing.text = osrsAccount.getSkillByName("Fishing").level.toString()
            tvCooking.text = osrsAccount.getSkillByName("Cooking").level.toString()
            tvFiremaking.text = osrsAccount.getSkillByName("Firemaking").level.toString()
            tvWoodcutting.text = osrsAccount.getSkillByName("Woodcutting").level.toString()
            tvFarming.text = osrsAccount.getSkillByName("Farming").level.toString()
        }
    }

    fun timeFormat(): SimpleDateFormat {
        return SimpleDateFormat("dd/MM/yyy H:m:s", Locale("nl_NL"))
    }

    private fun updateAccount(view: View) {
        Snackbar.make(view, "Getting new data for ${osrsAccount.username}...", Snackbar.LENGTH_SHORT).show()

        mainScope.launch {
            withContext(Dispatchers.IO) {
                // Get new hiscores
                osrsAccount.username?.let { it1 ->
                    Hiscores.getHiscoresFromUser(it1, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseBody = response.body
                                val content = responseBody?.string()

                                val skills: ArrayList<Skill> =
                                    Hiscores.getSkillsFromResponse(content)
                                val activities: ArrayList<Activity> =
                                    Hiscores.getActivitiesFromResponse(content)

                                osrsAccount.skills = skills
                                osrsAccount.activities = activities
                                osrsAccount.lastUpdate = Calendar.getInstance().time

                                mainScope.launch {
                                    withContext(Dispatchers.IO) {
                                        accountRepository.updateAccount(osrsAccount)
                                    }

                                    withContext(Dispatchers.Main) {
                                        tvLastUpdate.text = getString(R.string.last_update, timeFormat().format(osrsAccount.lastUpdate))

                                        tvAttack.text = osrsAccount.getSkillByName("Attack").level.toString()
                                        tvStrength.text = osrsAccount.getSkillByName("Strength").level.toString()
                                        tvDefence.text = osrsAccount.getSkillByName("Defence").level.toString()
                                        tvRanged.text = osrsAccount.getSkillByName("Ranged").level.toString()
                                        tvPrayer.text = osrsAccount.getSkillByName("Prayer").level.toString()
                                        tvMagic.text = osrsAccount.getSkillByName("Magic").level.toString()
                                        tvRunecraft.text = osrsAccount.getSkillByName("Runecrafting").level.toString()
                                        tvConstruction.text = osrsAccount.getSkillByName("Construction").level.toString()

                                        tvHitpoints.text = osrsAccount.getSkillByName("Hitpoints").level.toString()
                                        tvAgility.text = osrsAccount.getSkillByName("Agility").level.toString()
                                        tvHerblore.text = osrsAccount.getSkillByName("Herblore").level.toString()
                                        tvThieving.text = osrsAccount.getSkillByName("Thieving").level.toString()
                                        tvCrafting.text = osrsAccount.getSkillByName("Crafting").level.toString()
                                        tvFletching.text = osrsAccount.getSkillByName("Fletching").level.toString()
                                        tvSlayer.text = osrsAccount.getSkillByName("Slayer").level.toString()
                                        tvHunter.text = osrsAccount.getSkillByName("Hunter").level.toString()

                                        tvMining.text = osrsAccount.getSkillByName("Mining").level.toString()
                                        tvSmithing.text = osrsAccount.getSkillByName("Smithing").level.toString()
                                        tvFishing.text = osrsAccount.getSkillByName("Fishing").level.toString()
                                        tvCooking.text = osrsAccount.getSkillByName("Cooking").level.toString()
                                        tvFiremaking.text = osrsAccount.getSkillByName("Firemaking").level.toString()
                                        tvWoodcutting.text = osrsAccount.getSkillByName("Woodcutting").level.toString()
                                        tvFarming.text = osrsAccount.getSkillByName("Farming").level.toString()

                                        Snackbar.make(view, "Successfully updated ${osrsAccount.username}'s account!", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}