package com.example.beta.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListInt
import com.example.beta.database.converter.ListString

@Entity(tableName = "courses_table")
data class CoursesEnt (@PrimaryKey val course: String, val location: String ,val description: String
                       , val difficulty: Double , val distance: Double,val photos: ListInt
                       , val category: String, val activities: ListString)