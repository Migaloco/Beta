package com.example.beta.database.repository

import androidx.annotation.WorkerThread
import com.example.beta.database.dao.LocationsDao
import com.example.beta.database.entities.LocationsEntity

class LocationsRepository (private val locationsDao: LocationsDao){

    val allLocations = locationsDao.getAllLocations()

    @WorkerThread
    suspend fun insertAllLocations(list: List<LocationsEntity>){
        locationsDao.insertAllLocations(list)
    }

    @WorkerThread
    suspend fun deleteAllLocations(){
        locationsDao.deleteAllLocation()
    }
}