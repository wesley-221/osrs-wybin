package com.example.osrswybin.database.account_older_data

import android.content.Context
import com.example.osrswybin.database.RDatabase
import com.example.osrswybin.models.OSRSAccount

class AccountOlderDataRepository(context: Context) {
    private val accountOlderDataDao: AccountOlderDataDao

    init {
        val database = RDatabase.getDatabase(context)
        accountOlderDataDao = database!!.accountOlderData()
    }

    suspend fun getAllAccounts(): List<OSRSAccount> = accountOlderDataDao.getAllAccounts()
    suspend fun insertAccount(account: OSRSAccount) = accountOlderDataDao.insertAccount(account)
}