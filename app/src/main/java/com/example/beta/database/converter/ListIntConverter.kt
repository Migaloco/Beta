package com.example.beta.database.converter

import androidx.room.TypeConverter
import kotlin.collections.ArrayList

class ListIntConverter {

    @TypeConverter
    fun storedStringToList(string: String):ListInt{

        val photos = string.split(",")
        var array: ArrayList<Int> = arrayListOf()

        for(i in photos){

            if(i.isNotEmpty())array.add(i.toInt())
        }
        return ListInt(array)
    }

    @TypeConverter
    fun listToStoredString(photos:ListInt):String{

        var st = ""
        for(i in photos.list){
            st += "$i,"
        }

        return st
    }
}