package com.geeklabs.myscanner.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geeklabs.myscanner.models.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: User)

    @Query("Select * from User order by createdTime")
    fun getAll(): Single<MutableList<User>>
}