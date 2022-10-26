package com.geeklabs.myscanner.local

import androidx.room.TypeConverter
import com.geeklabs.myscanner.models.User
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromUser(user: User?): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun fromUser(value: String?): User {
        return Gson().fromJson(value, User::class.java)
    }
}