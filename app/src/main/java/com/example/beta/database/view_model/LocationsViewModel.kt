package com.example.beta.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.entities.LocationsEntity
import com.example.beta.database.repository.LocationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationsViewModel (application: Application): AndroidViewModel(application){

    private var repository: LocationsRepository
    var allLocations: LiveData<List<LocationsEntity>>

    init {

        val locationsDao = RoomDatabaseApp.getDatabase(application, viewModelScope).locationsDao()
        repository = LocationsRepository(locationsDao)
        allLocations = repository.allLocations
    }

    fun insertAllLocations(list: List<LocationsEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAllLocations(list)
    }

    fun deleteAllLocations() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllLocations()
    }
}