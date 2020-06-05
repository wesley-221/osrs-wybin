package com.example.osrswybin.models

import okhttp3.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor

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

        // Skill variables
        const val ATTACK = "Attack"
        const val DEFENCE = "Defence"
        const val STRENGTH = "Strength"
        const val HITPOINTS = "Hitpoints"
        const val RANGED = "Ranged"
        const val PRAYER = "Prayer"
        const val MAGIC = "Magic"
        const val COOKING = "Cooking"
        const val WOODCUTTING = "Woodcutting"
        const val FLETCHING = "Fletching"
        const val FISHING = "Fishing"
        const val FIREMAKING = "Firemaking"
        const val CRAFTING = "Crafting"
        const val SMITHING = "Smithing"
        const val MINING = "Mining"
        const val HERBLORE = "Herblore"
        const val AGILITY = "Agility"
        const val THIEVING = "Thieving"
        const val SLAYER = "Slayer"
        const val FARMING = "Farming"
        const val RUNECRAFTING = "Runecrafting"
        const val HUNTER  = "Hunter"
        const val CONSTRUCTION = "Construction"

        private val API_URL = "https://secure.runescape.com"
        private val HISCORE_URL = "$API_URL/m=hiscore_oldschool/index_lite.ws?player="

        fun getHiscoresFromUser(username: String, callback: Callback) {
            // Increase read time because api is slow
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

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

        fun calculateCombatLevel(osrsAccount: OSRSAccount): Int {
            val base = 0.25 * (osrsAccount.getSkillByName("Defence").level
                     + osrsAccount.getSkillByName("Hitpoints").level +
                    floor((osrsAccount.getSkillByName("Prayer").level / 2).toDouble()))

            val melee = 0.325 * (osrsAccount.getSkillByName("Attack").level + osrsAccount.getSkillByName("Strength").level)
            val range = 0.325 * (floor((3 * osrsAccount.getSkillByName("Ranged").level / 2).toDouble()))
            val mage = 0.325 * (floor((3 * osrsAccount.getSkillByName("Magic").level / 2).toDouble()))

            return floor(base + maxOf(melee, range, mage)).toInt()
        }
    }
}