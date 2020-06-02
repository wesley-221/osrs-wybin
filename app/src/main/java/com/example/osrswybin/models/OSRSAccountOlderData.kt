package com.example.osrswybin.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "osrs_account_older_data_table")
data class OSRSAccountOlderData (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var osrsAccount: OSRSAccount
) : Parcelable