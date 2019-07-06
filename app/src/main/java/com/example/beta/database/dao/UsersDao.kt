package com.example.beta.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beta.database.entities.UsersEntity

@Dao
interface UsersDao{

    @Query("SELECT * from users_table ORDER BY points ASC")
    fun getAllUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(list: List<UsersEntity>)

    @Query("DELETE FROM users_table")
    suspend fun deleteAllUsers()
}
