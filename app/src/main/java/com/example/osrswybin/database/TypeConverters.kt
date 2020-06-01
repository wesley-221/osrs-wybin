package com.example.osrswybin.database

import androidx.room.TypeConverter
import com.example.osrswybin.models.Activity
import com.example.osrswybin.models.Skill
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class TypeConverters {
    @TypeConverter
    fun skillsFromString(value: String):  ArrayList<Skill> {
        val listType: Type = object : TypeToken<ArrayList<Skill>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun skillsToString(skills: ArrayList<Skill>): String {
        return Gson().toJson(skills)
    }

    @TypeConverter
    fun activitiesFromString(value: String): ArrayList<Activity> {
        val listType: Type = object : TypeToken<ArrayList<Activity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun activitiesToString(activities: ArrayList<Activity>): String {
        return Gson().toJson(activities)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}