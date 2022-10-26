package com.geeklabs.myscanner.local

import com.geeklabs.myscanner.models.User
import javax.inject.Inject

open class LocalDataSource @Inject constructor(private val appDatabaseWrapper: AppDatabaseWrapper) {

    private val appDatabase: AppDatabase get() = appDatabaseWrapper.getAppDatabase()


    fun saveUser(user: User) = appDatabase.userDao().save(user)
    fun getAll() = appDatabase.userDao().getAll()

}