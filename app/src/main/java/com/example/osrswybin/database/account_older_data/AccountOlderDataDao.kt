package com.example.osrswybin.database.account_older_data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.osrswybin.models.OSRSAccountOlderData
import java.util.*

@Dao
interface AccountOlderDataDao {
    @Query("SELECT * FROM osrs_account_older_data_table")
    suspend fun getAllAccounts(): List<OSRSAccountOlderData>

    @Query("SELECT * FROM osrs_account_older_data_table WHERE username = :username AND lastUpdate <= :date ORDER BY lastUpdate DESC")
    suspend fun getClosestFromDate(username: String, date: Date): OSRSAccountOlderData

    @Insert
    suspend fun insertAccount(account: OSRSAccountOlderData)

    @Query("SELECT * FROM osrs_account_older_data_table WHERE username = :username ORDER BY lastUpdate DESC LIMIT 1")
    suspend fun getOldestData(username: String): OSRSAccountOlderData
}