package com.example.beta.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beta.database.converter.ListInt
import com.example.beta.database.converter.ListString

@Entity(tableName = "courses_table")
data class CoursesEnt (@PrimaryKey val course: String, val district: String, val description: String
                       , val difficulty: Int, val waypoints: String, val wMarkers: ListString
                       , val wDescriptions: ListString, val start:String, val finish:String
                       , val category: String)