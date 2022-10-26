package com.geeklabs.myscanner.local

import android.content.Context
import androidx.room.Room
import javax.inject.Inject

class AppDatabaseWrapper @Inject constructor(private val context: Context) {

    private var appDatabaseInstance: AppDatabase? = null

    @Synchronized
    fun getAppDatabase(): AppDatabase {
        if (appDatabaseInstance == null) {
            val builder = Room.databaseBuilder(context, AppDatabase::class.java, "ScanNGet.db")
            appDatabaseInstance = builder.fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        }
        return appDatabaseInstance!!
    }

    fun closeDB() {
        appDatabaseInstance?.close()
        appDatabaseInstance = null
    }
}