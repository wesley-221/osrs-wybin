package com.example.osrswybin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.osrswybin.database.account.AccountDao
import com.example.osrswybin.database.account_older_data.AccountOlderDataDao
import com.example.osrswybin.models.OSRSAccount
import com.example.osrswybin.models.OSRSAccountOlderData

@Database(entities = [OSRSAccount::class, OSRSAccountOlderData::class], version = 2, exportSchema = false)
@TypeConverters(com.example.osrswybin.database.TypeConverters::class)
abstract class RDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun accountOlderData(): AccountOlderDataDao

    companion object {
        private const val DATABASE_NAME = "osrs_wybin"

        @Volatile
        private var accountsRoomDatabaseInstance: RDatabase? = null

        fun getDatabase(context: Context): RDatabase? {
            if(accountsRoomDatabaseInstance == null) {
                synchronized(RDatabase::class.java) {
                    if(accountsRoomDatabaseInstance == null) {
                        accountsRoomDatabaseInstance = Room
                            .databaseBuilder(context.applicationContext, RDatabase::class.java,
                                DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration() // nuke old databases
                            .build()
                    }
                }
            }

            return accountsRoomDatabaseInstance
        }
    }
}