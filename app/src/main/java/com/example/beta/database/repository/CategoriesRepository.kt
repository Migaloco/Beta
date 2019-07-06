package com.example.beta.database.repository

import androidx.annotation.WorkerThread
import com.example.beta.database.dao.CategoriesDao
import com.example.beta.database.entities.CategoriesEntity

class CategoriesRepository (private val categoryDao: CategoriesDao){

    val allCategories = categoryDao.getAll()

    @WorkerThread
    suspend fun insertAll(list:List<CategoriesEntity>){

        categoryDao.insertAll(list)
    }

    @WorkerThread
    suspend fun deleteAll(){

        categoryDao.deleteAll()
    }
}