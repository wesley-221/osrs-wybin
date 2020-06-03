package com.example.osrswybin.ui.tracking

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.example.osrswybin.R
import com.example.osrswybin.database.account.AccountRepository
import com.example.osrswybin.database.account_older_data.AccountOlderDataRepository
import com.example.osrswybin.models.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_account_overview.*
import kotlinx.android.synthetic.main.skills_overview_first_row.*
import kotlinx.android.synthetic.main.skills_overview_first_row.view.*
import kotlinx.android.synthetic.main.skills_overview_second_row.*
import kotlinx.android.synthetic.main.skills_overview_second_row.view.*
import kotlinx.android.synthetic.main.skills_overview_third_row.*
import kotlinx.android.synthetic.main.skills_overview_third_row.view.*
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
    private lateinit var accountOlderDataRepository: AccountOlderDataRepository
    private lateinit var osrsAccount: OSRSAccount

    private var isPopupOpen: Boolean = false
    private lateinit var popup: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_overview)

        // Show back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        accountId = intent.getIntExtra("accountId", -1)
        accountRepository = AccountRepository(this)
        accountOlderDataRepository = AccountOlderDataRepository(this)

        btnUpdate.setOnClickListener { updateAccount(it) }

        setupButtonListeners()

        mainScope.launch {
            withContext(Dispatchers.IO) {
                osrsAccount = accountRepository.getAccountById(accountId)
            }

            actionBar?.title = osrsAccount.username
            tvLastUpdate.text = getString(R.string.last_update, timeFormat().format(osrsAccount.lastUpdate))

            tvAttack.text = osrsAccount.getSkillByName(Hiscores.ATTACK).level.toString()
            tvStrength.text = osrsAccount.getSkillByName(Hiscores.STRENGTH).level.toString()
            tvDefence.text = osrsAccount.getSkillByName(Hiscores.DEFENCE).level.toString()
            tvRanged.text = osrsAccount.getSkillByName(Hiscores.RANGED).level.toString()
            tvPrayer.text = osrsAccount.getSkillByName(Hiscores.PRAYER).level.toString()
            tvMagic.text = osrsAccount.getSkillByName(Hiscores.MAGIC).level.toString()
            tvRunecraft.text = osrsAccount.getSkillByName(Hiscores.RUNECRAFTING).level.toString()
            tvConstruction.text = osrsAccount.getSkillByName(Hiscores.CONSTRUCTION).level.toString()

            tvHitpoints.text = osrsAccount.getSkillByName(Hiscores.HITPOINTS).level.toString()
            tvAgility.text = osrsAccount.getSkillByName(Hiscores.AGILITY).level.toString()
            tvHerblore.text = osrsAccount.getSkillByName(Hiscores.HERBLORE).level.toString()
            tvThieving.text = osrsAccount.getSkillByName(Hiscores.THIEVING).level.toString()
            tvCrafting.text = osrsAccount.getSkillByName(Hiscores.CRAFTING).level.toString()
            tvFletching.text = osrsAccount.getSkillByName(Hiscores.FLETCHING).level.toString()
            tvSlayer.text = osrsAccount.getSkillByName(Hiscores.SLAYER).level.toString()
            tvHunter.text = osrsAccount.getSkillByName(Hiscores.HUNTER).level.toString()

            tvMining.text = osrsAccount.getSkillByName(Hiscores.MINING).level.toString()
            tvSmithing.text = osrsAccount.getSkillByName(Hiscores.MINING).level.toString()
            tvFishing.text = osrsAccount.getSkillByName(Hiscores.FISHING).level.toString()
            tvCooking.text = osrsAccount.getSkillByName(Hiscores.COOKING).level.toString()
            tvFiremaking.text = osrsAccount.getSkillByName(Hiscores.FIREMAKING).level.toString()
            tvWoodcutting.text = osrsAccount.getSkillByName(Hiscores.WOODCUTTING).level.toString()
            tvFarming.text = osrsAccount.getSkillByName(Hiscores.FARMING).level.toString()
        }
    }

    fun timeFormat(): SimpleDateFormat {
        return SimpleDateFormat("dd/MM/yyy H:mm:ss", Locale("nl_NL"))
    }

    private fun updateAccount(view: View) {
        Snackbar.make(view, "Getting new data for ${osrsAccount.username}...", Snackbar.LENGTH_SHORT).show()

        mainScope.launch {
            withContext(Dispatchers.IO) {
                // Get new hiscores
                osrsAccount.username?.let { it1 ->
                    Hiscores.getHiscoresFromUser(it1, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Snackbar.make(view, "Unable to load data: ${e.message}", Snackbar.LENGTH_LONG).show()
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

                                mainScope.launch {
                                    withContext(Dispatchers.IO) {
                                        // Save the old data so we can compare this
                                        accountOlderDataRepository.insertAccount(OSRSAccountOlderData(0, osrsAccount.username, osrsAccount.skills, osrsAccount.activities, osrsAccount.lastUpdate))
                                    }

                                    withContext(Dispatchers.IO) {
                                        // Update with the newly retrieved data
                                        osrsAccount.skills = skills
                                        osrsAccount.activities = activities
                                        osrsAccount.lastUpdate = Calendar.getInstance().time

                                        accountRepository.updateAccount(osrsAccount)
                                    }

                                    withContext(Dispatchers.Main) {
                                        tvLastUpdate.text = getString(R.string.last_update, timeFormat().format(osrsAccount.lastUpdate))

                                        tvAttack.text = osrsAccount.getSkillByName(Hiscores.ATTACK).level.toString()
                                        tvStrength.text = osrsAccount.getSkillByName(Hiscores.STRENGTH).level.toString()
                                        tvDefence.text = osrsAccount.getSkillByName(Hiscores.DEFENCE).level.toString()
                                        tvRanged.text = osrsAccount.getSkillByName(Hiscores.RANGED).level.toString()
                                        tvPrayer.text = osrsAccount.getSkillByName(Hiscores.PRAYER).level.toString()
                                        tvMagic.text = osrsAccount.getSkillByName(Hiscores.MAGIC).level.toString()
                                        tvRunecraft.text = osrsAccount.getSkillByName(Hiscores.RUNECRAFTING).level.toString()
                                        tvConstruction.text = osrsAccount.getSkillByName(Hiscores.CONSTRUCTION).level.toString()

                                        tvHitpoints.text = osrsAccount.getSkillByName(Hiscores.HITPOINTS).level.toString()
                                        tvAgility.text = osrsAccount.getSkillByName(Hiscores.AGILITY).level.toString()
                                        tvHerblore.text = osrsAccount.getSkillByName(Hiscores.HERBLORE).level.toString()
                                        tvThieving.text = osrsAccount.getSkillByName(Hiscores.THIEVING).level.toString()
                                        tvCrafting.text = osrsAccount.getSkillByName(Hiscores.CRAFTING).level.toString()
                                        tvFletching.text = osrsAccount.getSkillByName(Hiscores.FLETCHING).level.toString()
                                        tvSlayer.text = osrsAccount.getSkillByName(Hiscores.SLAYER).level.toString()
                                        tvHunter.text = osrsAccount.getSkillByName(Hiscores.HUNTER).level.toString()

                                        tvMining.text = osrsAccount.getSkillByName(Hiscores.MINING).level.toString()
                                        tvSmithing.text = osrsAccount.getSkillByName(Hiscores.MINING).level.toString()
                                        tvFishing.text = osrsAccount.getSkillByName(Hiscores.FISHING).level.toString()
                                        tvCooking.text = osrsAccount.getSkillByName(Hiscores.COOKING).level.toString()
                                        tvFiremaking.text = osrsAccount.getSkillByName(Hiscores.FIREMAKING).level.toString()
                                        tvWoodcutting.text = osrsAccount.getSkillByName(Hiscores.WOODCUTTING).level.toString()
                                        tvFarming.text = osrsAccount.getSkillByName(Hiscores.FARMING).level.toString()

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

    private fun showExperienceGained(username: String, skill: String) {
        if(this.isPopupOpen) {
            popup.dismiss()
        }

        this.isPopupOpen = true

        // Create the popup modal and elements
        popup = PopupWindow(this)
        val layout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.orientation = LinearLayout.VERTICAL

        // Create close button
        val btnClose = Button(this)
        btnClose.text = getString(R.string.close)

        btnClose.setOnClickListener {
            this.isPopupOpen = false
            popup.dismiss()
        }

        mainScope.launch {
            val osrsAccountOldestData = withContext(Dispatchers.IO) {
                accountOlderDataRepository.getOldestData(username)
            }

            // Calculate the experience gained
            val experienceGained = osrsAccount.getSkillByName(skill).experience - osrsAccountOldestData.getSkillByName(skill).experience

            // Create header text
            val headerText = TextView(applicationContext)
            headerText.text = getString(R.string.experience_gained, skill, timeFormat().format(osrsAccountOldestData.dataFrom))
            headerText.textSize = 18f
            headerText.gravity = Gravity.CENTER
            headerText.setTextColor(Color.WHITE)

            // Create experience gained text
            val experienceGainedText = TextView(applicationContext)
            experienceGainedText.text = getString(R.string.experience_gained_string, if(experienceGained > 0) { "+" } else { "" }, experienceGained)
            experienceGainedText.setTextColor(Color.WHITE)

            // Add items to the view
            layout.addView(headerText, layoutParams)
            layout.addView(experienceGainedText, layoutParams)
            layout.addView(btnClose, layoutParams)

            layout.setPadding(8)

            // Connect the layout to the popup and show it
            popup.contentView = layout
            popup.showAtLocation(layout, Gravity.BOTTOM, 10, 10)
        }
    }

    private fun setupButtonListeners() {
        skills_first_row_clickable.ivAttack.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.ATTACK) }
        }

        skills_first_row_clickable.ivStrength.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.STRENGTH) }
        }

        skills_first_row_clickable.ivDefence.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.DEFENCE) }
        }

        skills_first_row_clickable.ivRanged.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.RANGED) }
        }

        skills_first_row_clickable.ivPrayer.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.PRAYER) }
        }

        skills_first_row_clickable.ivMagic.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.MAGIC) }
        }

        skills_first_row_clickable.ivRunecraft.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.RUNECRAFTING) }
        }

        skills_first_row_clickable.ivConstruction.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.CONSTRUCTION) }
        }

        skills_second_row_clickable.ivHitpoints.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.HITPOINTS) }
        }

        skills_second_row_clickable.ivAgility.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.AGILITY) }
        }

        skills_second_row_clickable.ivHerblore.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.HERBLORE) }
        }

        skills_second_row_clickable.ivThieving.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.THIEVING) }
        }

        skills_second_row_clickable.ivCrafting.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.CRAFTING) }
        }

        skills_second_row_clickable.ivFletching.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.FLETCHING) }
        }

        skills_second_row_clickable.ivSlayer.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.SLAYER) }
        }

        skills_second_row_clickable.ivHunter.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.HUNTER) }
        }

        skills_third_row_clickable.ivMining.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.MINING) }
        }

        skills_third_row_clickable.ivSmithing.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.SMITHING) }
        }

        skills_third_row_clickable.ivFishing.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.FISHING) }
        }

        skills_third_row_clickable.ivCooking.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.COOKING) }
        }

        skills_third_row_clickable.ivFiremaking.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.FIREMAKING) }
        }

        skills_third_row_clickable.ivWoodcutting.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.WOODCUTTING) }
        }

        skills_third_row_clickable.ivFarming.setOnClickListener {
            osrsAccount.username?.let { it1 -> showExperienceGained(it1, Hiscores.FARMING) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}