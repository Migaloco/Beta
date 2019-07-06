package com.example.beta.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListInt

@Entity(tableName = "locations_table")
data class LocationsEntity (@PrimaryKey val name:String, val description: String, val photo:Int)