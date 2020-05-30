package com.example.osrswybin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Activity (
    val name: String,
    val rank: Int,
    val kills: Int
): Parcelable