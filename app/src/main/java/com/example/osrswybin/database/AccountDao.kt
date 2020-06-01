package com.example.osrswybin.database

import androidx.room.*
import com.example.osrswybin.models.OSRSAccount

@Dao
interface AccountDao {
    @Query("SELECT * FROM osrs_account_table")
    suspend fun getAllAccounts(): List<OSRSAccount>

    @Insert
    suspend fun insertAccount(account: OSRSAccount)

    @Delete
    suspend fun deleteAccount(account: OSRSAccount)

    @Query("SELECT * FROM osrs_account_table WHERE id = :accountId")
    suspend fun getAccountById(accountId: Int): OSRSAccount

    @Update
    suspend fun updateAccount(osrsAccount: OSRSAccount)
}