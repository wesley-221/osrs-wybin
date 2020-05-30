package com.example.osrswybin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.osrswybin.models.OSRSAccount

@Dao
interface AccountDao {
    @Query("SELECT * FROM osrs_account_table")
    suspend fun getAllAccounts(): List<OSRSAccount>

    @Insert
    suspend fun insertAccount(account: OSRSAccount)
}