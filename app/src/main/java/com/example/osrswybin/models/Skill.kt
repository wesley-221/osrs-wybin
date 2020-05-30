package com.example.osrswybin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Skill(
    val name: String,
    val rank: Int,
    val level: Int,
    val experience: Int
): Parcelable