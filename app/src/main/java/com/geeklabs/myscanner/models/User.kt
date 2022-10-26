package com.geeklabs.myscanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class User : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var rawData: String = ""
    var createdTime: Long = 0
}
