package com.example.osrswybin.database.account_older_data

import android.content.Context
import com.example.osrswybin.database.RDatabase
import com.example.osrswybin.models.OSRSAccountOlderData
import java.util.*

class AccountOlderDataRepository(context: Context) {
    private val accountOlderDataDao: AccountOlderDataDao

    init {
        val database = RDatabase.getDatabase(context)
        accountOlderDataDao = database!!.accountOlderData()
    }

    suspend fun getAllAccounts(): List<OSRSAccountOlderData> = accountOlderDataDao.getAllAccounts()
    suspend fun getClosestFromDate(username: String, date: Date): OSRSAccountOlderData = accountOlderDataDao.getClosestFromDate(username, date)
    suspend fun insertAccount(account: OSRSAccountOlderData) = accountOlderDataDao.insertAccount(account)
    suspend fun getOldestData(username: String): OSRSAccountOlderData = accountOlderDataDao.getOldestData(username)
}