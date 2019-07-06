package com.example.beta.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.repository.CoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoursesViewModel (application: Application): AndroidViewModel(application){

    private var repository: CoursesRepository
    var allCourses: LiveData<List<CoursesEnt>>
    var singleCourse:LiveData<CoursesEnt>

    init {

        val coursesDao = RoomDatabaseApp.getDatabase(application, viewModelScope).coursesDao()
        repository = CoursesRepository(coursesDao)
        allCourses = repository.allCourses
        singleCourse = repository.singleCourse
    }

    fun getCourse(str: String) = viewModelScope.launch(Dispatchers.IO) {

        repository.getCourse(str)
    }

    fun insertAll(list: List<CoursesEnt>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(list)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}