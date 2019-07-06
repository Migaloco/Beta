package com.example.beta.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.beta.database.entities.LocationsEntity

@Dao
interface LocationsDao{

    @Query("SELECT * FROM locations_table")
    fun getAllLocations(): LiveData<List<LocationsEntity>>

    @Insert
    suspend fun insertAllLocations(list: List<LocationsEntity>)

    @Query("DELETE FROM locations_table")
    suspend fun deleteAllLocation()
}