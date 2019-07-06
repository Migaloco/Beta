package com.example.beta.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListString

@Entity(tableName = "users_table")
data class UsersEntity(@PrimaryKey val name: String, val points: Int, val coursesDone: ListString, val coursesToDo: ListString)