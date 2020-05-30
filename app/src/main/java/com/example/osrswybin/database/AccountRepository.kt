package com.example.osrswybin.database

import android.content.Context
import com.example.osrswybin.models.OSRSAccount

class AccountRepository(context: Context) {
    private val accountDao: AccountDao

    init {
        val database = AccountRoomDatabase.getDatabase(context)
        accountDao = database!!.accountDao()
    }

    suspend fun getAllAccounts(): List<OSRSAccount> = accountDao.getAllAccounts()
    suspend fun insertAccount(account: OSRSAccount) = accountDao.insertAccount(account)
}