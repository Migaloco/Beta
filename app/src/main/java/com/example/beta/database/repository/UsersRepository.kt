package com.example.beta.database.repository

import androidx.annotation.WorkerThread
import com.example.beta.database.dao.UsersDao
import com.example.beta.database.entities.UsersEntity

class UsersRepository (private val usersDao: UsersDao){

    val allUsers = usersDao.getAllUsers()

    @WorkerThread
    suspend fun insertAllUsers(list: List<UsersEntity>){

        usersDao.insertAllUsers(list)
    }

    @WorkerThread
    suspend fun deleteAllUsers(){

        usersDao.deleteAllUsers()
    }
}