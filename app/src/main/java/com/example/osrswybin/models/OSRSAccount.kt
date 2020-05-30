package com.example.osrswybin.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "osrs_account_table")
data class OSRSAccount(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var username: String?,
    var skills: ArrayList<Skill>,
    var activities: ArrayList<Activity>
): Parcelable {
    fun getSkillByName(skill: String): Skill {
        return skills.first { it.name == skill }
    }

    fun getActivityByName(activity: String): Activity {
        return activities.first { it.name == activity }
    }
}