package com.example.osrswybin.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@Entity(tableName = "osrs_account_older_data_table")
data class OSRSAccountOlderData (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var username: String?,
    var skills: ArrayList<Skill>,
    var activities: ArrayList<Activity>,
    @ColumnInfo(name = "lastUpdate")
    var dataFrom: Date
) : Parcelable {
    fun getSkillByName(skill: String): Skill {
        return skills.first { it.name == skill }
    }

    fun getActivityByName(activity: String): Activity {
        return activities.first { it.name == activity }
    }
}