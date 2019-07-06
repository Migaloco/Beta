package com.example.beta.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.entities.CategoriesEntity
import com.example.beta.database.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel (application: Application): AndroidViewModel(application){

    private var repository: CategoriesRepository
    var allCategories: LiveData<List<CategoriesEntity>>

    init {
        val categoryDao = RoomDatabaseApp.getDatabase(application, viewModelScope).categoriesDao()
        repository = CategoriesRepository(categoryDao)
        allCategories = repository.allCategories
    }

    fun insertAll(list: List<CategoriesEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(list)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}