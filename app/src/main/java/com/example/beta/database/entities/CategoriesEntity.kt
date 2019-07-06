package com.example.beta.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories_table")
data class CategoriesEntity (@PrimaryKey val category: String, val icon: Int)