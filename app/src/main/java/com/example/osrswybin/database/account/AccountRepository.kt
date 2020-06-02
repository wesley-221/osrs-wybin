package com.example.osrswybin.database.account

import android.content.Context
import com.example.osrswybin.database.RDatabase
import com.example.osrswybin.models.OSRSAccount

class AccountRepository(context: Context) {
    private val accountDao: AccountDao

    init {
        val database = RDatabase.getDatabase(context)
        accountDao = database!!.accountDao()
    }

    suspend fun getAllAccounts(): List<OSRSAccount> = accountDao.getAllAccounts()
    suspend fun insertAccount(account: OSRSAccount) = accountDao.insertAccount(account)
    suspend fun deleteAccount(account: OSRSAccount) = accountDao.deleteAccount(account)
    suspend fun getAccountById(accountId: Int) = accountDao.getAccountById(accountId)
    suspend fun updateAccount(osrsAccount: OSRSAccount) = accountDao.updateAccount(osrsAccount)
}