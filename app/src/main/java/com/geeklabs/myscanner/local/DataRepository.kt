package com.geeklabs.myscanner.local

import com.geeklabs.myscanner.models.User
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {
    fun saveUser(user: User) = localDataSource.saveUser(user)
    fun getAll() = localDataSource.getAll()
}