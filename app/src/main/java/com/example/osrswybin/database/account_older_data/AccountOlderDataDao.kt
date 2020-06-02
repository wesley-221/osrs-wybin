package com.example.osrswybin.database.account_older_data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.osrswybin.models.OSRSAccount

@Dao
interface AccountOlderDataDao {
    @Query("SELECT * FROM osrs_account_table")
    suspend fun getAllAccounts(): List<OSRSAccount>

    @Insert
    suspend fun insertAccount(account: OSRSAccount)
}