package com.example.beta.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListString

@Entity(tableName = "users_table")
data class UsersEntity(@PrimaryKey val name: String, var points: Int = 0, var coursesDone: ListString = ListString(
    arrayListOf())
)