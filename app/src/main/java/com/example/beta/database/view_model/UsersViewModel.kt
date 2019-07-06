package com.example.beta.database.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.entities.UsersEntity
import com.example.beta.database.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersViewModel (application: Application): AndroidViewModel(application){

    private var repository:UsersRepository
    var allUsers: LiveData<List<UsersEntity>>

    init {

        val userDao = RoomDatabaseApp.getDatabase(application, viewModelScope).usersDao()
        repository = UsersRepository(userDao)
        allUsers = repository.allUsers
    }

    fun insertAllUsers(list: List<UsersEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAllUsers(list)
    }

    fun deleteAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllUsers()
    }
}