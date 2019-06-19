package com.example.beta.others

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    val dbName = "MyCategories"
    val dbTable = "Categories"
    val colId = "ID"
    val colTitle = "Title"
    val dbVersion = 1

    // Create Table if not exists MyCategories(id INTEGER PRIMARY KEY, title TEXT, description TEXT);
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + colTitle + " TEXT);"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context){

        var db = DataBaseHelperCategories(context)
        sqlDB = db.writableDatabase
    }

    inner class DataBaseHelperCategories: SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context):super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0?.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database initiated", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

            p0?.execSQL("Drop table IF EXISTS" + dbTable)
        }
    }

    fun insert(st: String):Boolean{

        var values = ContentValues()
        values.put(colTitle, st)

        val iD = sqlDB!!.insert(dbTable, "", values)

        return iD.toInt() != -1
    }

    fun query(projection:Array<String>, selection: String, selectionArgs:Array<String>, sorOrder:String): Cursor {

        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
        return cursor
    }
}