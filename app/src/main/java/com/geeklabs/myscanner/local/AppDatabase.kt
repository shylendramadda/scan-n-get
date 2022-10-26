package com.geeklabs.myscanner.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geeklabs.myscanner.local.dao.UserDao
import com.geeklabs.myscanner.models.User

/**
 * Created by Shanmuka on 6/24/2019.
 */
@Database(
    entities = [User::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}