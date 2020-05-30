package com.example.osrswybin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.osrswybin.models.OSRSAccount

@Database(entities = [OSRSAccount::class], version = 1, exportSchema = false)
@TypeConverters(ArrayConverter::class)
abstract class AccountRoomDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao

    companion object {
        private const val DATABASE_NAME = "osrs_wybin"

        @Volatile
        private var accountsRoomDatabaseInstance: AccountRoomDatabase? = null

        fun getDatabase(context: Context): AccountRoomDatabase? {
            if(accountsRoomDatabaseInstance == null) {
                synchronized(AccountRoomDatabase::class.java) {
                    if(accountsRoomDatabaseInstance == null) {
                        accountsRoomDatabaseInstance = Room.databaseBuilder(context.applicationContext, AccountRoomDatabase::class.java, DATABASE_NAME).build()
                    }
                }
            }

            return accountsRoomDatabaseInstance
        }
    }
}