package com.example.beta.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beta.database.entities.CoursesEnt

@Dao
interface CoursesDao {

    @Query("SELECT * from courses_table ORDER BY course ASC")
    fun getAllCourses(): LiveData<List<CoursesEnt>>

    @Query("SELECT * FROM courses_table WHERE course LIKE :str")
    fun getCourse(str: String):LiveData<CoursesEnt>

    @Query("SELECT * FROM courses_table WHERE category LIKE :category")
    fun getAllCoursesByCtg(category: String):LiveData<List<CoursesEnt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CoursesEnt>)

    @Query("DELETE FROM courses_table")
    suspend fun deleteAll()
}