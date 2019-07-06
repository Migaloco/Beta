package com.example.beta.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.beta.data.ExampleCourses
import com.example.beta.database.converter.ListIntConverter
import com.example.beta.database.converter.ListStringConverter
import com.example.beta.database.dao.CategoriesDao
import com.example.beta.database.dao.CoursesDao
import com.example.beta.database.dao.LocationsDao
import com.example.beta.database.dao.UsersDao
import com.example.beta.database.entities.CategoriesEntity
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.entities.LocationsEntity
import com.example.beta.database.entities.UsersEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [CoursesEnt::class, CategoriesEntity::class, UsersEntity::class, LocationsEntity::class], version = 4)
@TypeConverters(ListIntConverter::class, ListStringConverter::class)
abstract class RoomDatabaseApp: RoomDatabase(){

    abstract fun coursesDao() : CoursesDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun usersDao(): UsersDao
    abstract fun locationsDao(): LocationsDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDatabaseApp? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RoomDatabaseApp {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabaseApp::class.java,
                    "app_database").fallbackToDestructiveMigration().addCallback(CoursesDatabaseCallback(scope)).build()
                    INSTANCE = instance
                instance
            }
        }

        private class CoursesDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch {
                        populateCourses(database.coursesDao())
                        populateCategories(database.categoriesDao())
                        populateUsers(database.usersDao())
                        populateLocations(database.locationsDao())
                    }
                }
            }
        }


        suspend fun populateCourses(coursesDao: CoursesDao) {

            coursesDao.deleteAll()

            val list = ExampleCourses().getExamplesCourses()

            coursesDao.insertAll(list)
        }

        suspend fun populateCategories(categoriesDao: CategoriesDao){

            categoriesDao.deleteAll()

            val list = ExampleCourses().getExamplesCategories()

            categoriesDao.insertAll(list)
        }

        suspend fun populateUsers(usersDao: UsersDao){

            usersDao.deleteAllUsers()

            val list = ExampleCourses().getExamplesUsers()

            usersDao.insertAllUsers(list)
        }

        suspend fun populateLocations(locationsDao: LocationsDao){

            locationsDao.deleteAllLocation()

            val list = ExampleCourses().getExamplesLocations()

            locationsDao.insertAllLocations(list)
        }
    }
}