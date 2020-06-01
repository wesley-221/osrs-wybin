package com.example.osrswybin.models

import kotlinx.android.synthetic.main.skills_overview_first_row.*
import kotlinx.android.synthetic.main.skills_overview_second_row.*
import kotlinx.android.synthetic.main.skills_overview_third_row.*
import okhttp3.*

class Hiscores {
    companion object {
        private val SKILLS = mutableListOf(
            "Overall", "Attack", "Defence",
            "Strength", "Hitpoints", "Ranged",
            "Prayer", "Magic", "Cooking",
            "Woodcutting", "Fletching", "Fishing",
            "Firemaking", "Crafting", "Smithing",
            "Mining", "Herblore", "Agility",
            "Thieving", "Slayer", "Farming",
            "Runecrafting", "Hunter", "Construction"
        )

        private val ACTIVITIES = mutableListOf(
            "Bounty Hunter - Hunter","Bounty Hunter - Rogue","Clue Scrolls (all)",
            "Clue Scrolls (beginner)","Clue Scrolls (easy)","Clue Scrolls (medium)",
            "Clue Scrolls (hard)","Clue Scrolls (elite)","Clue Scrolls (master)",
            "League Points","Abyssal Sire","Alchemical Hydra",
            "Barrows Chests","Bryophyta","Callisto",
            "Cerberus","Chambers of Xeric","Chambers of Xeric: Challenge Mode",
            "Chaos Elemental","Chaos Fanatic","Commander Zilyana",
            "Corporeal Beast","Crazy Archaeologist","Dagannoth Prime",
            "Dagannoth Rex","Dagannoth Supreme","Deranged Archaeologist",
            "General Graardor","Giant Mole","Grotesque Guardians",
            "Hespori","Kalphite Queen","King Black Dragon",
            "Kraken","Kree'Arra","K'ril Tsutsaroth",
            "Mimic","Nightmare","Obor",
            "Sarachnis","Scorpia","Skotizo",
            "The Gauntlet","The Corrupted Gauntlet","Theatre of Blood",
            "Thermonuclear Smoke Devil","TzKal-Zuk","TzTok-Jad",
            "Venenatis","Vet'ion","Vorkath",
            "Wintertodt","Zalcano","Zulrah"
        )

        private val API_URL = "https://secure.runescape.com"
        private val HISCORE_URL = "$API_URL/m=hiscore_oldschool/index_lite.ws?player="

        fun getHiscoresFromUser(username: String, callback: Callback) {
            val client = OkHttpClient()
            val request = Request.Builder().url("$HISCORE_URL$username").build()

            client.newCall(request).enqueue(callback)
        }

        fun getSkillsFromResponse(response: String?): ArrayList<Skill> {
            val skills: ArrayList<Skill> = arrayListOf()
            var iteration = 0

            response?.split("\n")?.forEach { line ->
                // Ignore the activities
                if(iteration < SKILLS.size) {
                    // Split the data by a comma
                    val data = line.split(",").map { it.toInt() }.toIntArray()

                    // Create a new skill object
                    skills.add(Skill(SKILLS[iteration], data[0], data[1], data[2]))
                }

                iteration ++
            }

            return skills
        }

        fun getActivitiesFromResponse(response: String?): ArrayList<Activity> {
            val activities: ArrayList<Activity> = arrayListOf()
            var globalIteration = 0
            var activityIteration = 0

            response?.split("\n")?.forEach { line ->
                if(globalIteration > SKILLS.size) {
                    // Check for empty line
                    if(line != "") {
                        // Split the data by a comma
                        val data = line.split(",").map { it.toInt() }.toIntArray()

                        // Create a new activity object
                        activities.add(Activity(ACTIVITIES[activityIteration], data[0], data[1]))

                        activityIteration ++
                    }
                }

                globalIteration ++
            }

            return activities
        }
    }
}