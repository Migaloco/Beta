package com.example.beta.data

import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListString

data class UsersData(val name: String, var points: Int = 0, var coursesDone: ListString = ListString(arrayListOf()))