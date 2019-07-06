package com.example.beta.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.beta.database.dao.CoursesDao
import com.example.beta.database.entities.CoursesEnt

class CoursesRepository(private val coursesDao: CoursesDao){

    val allCourses: LiveData<List<CoursesEnt>> = coursesDao.getAllCourses()
    var singleCourse: LiveData<CoursesEnt> = coursesDao.getCourse("")

    @WorkerThread
    fun getCourse(str: String) {

        singleCourse = coursesDao.getCourse(str)
    }

    @WorkerThread
    suspend fun insertAll(coursesEnt: List<CoursesEnt>) {
        coursesDao.insertAll(coursesEnt)
    }

    @WorkerThread
    suspend fun deleteAll(){
        coursesDao.deleteAll()
    }
}