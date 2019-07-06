package com.example.beta.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.beta.database.entities.CategoriesEntity

@Dao
interface CategoriesDao {

    @Query("SELECT * from categories_table")
    fun getAll(): LiveData<List<CategoriesEntity>>

    @Insert
    suspend fun insertAll(list: List<CategoriesEntity>)

    @Query("DELETE from categories_table")
    suspend fun deleteAll()
}