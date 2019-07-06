package com.example.beta.database.converter

import androidx.room.TypeConverter

class ListStringConverter {

    @TypeConverter
    fun listToStoredString(list: ListString):String{

        var str = ""
        for(i in list.list){
            str += "$i,"
        }
        return str
    }

    @TypeConverter
    fun storedStringToList(str: String):ListString{

        val courses = str.split(",")
        val array = arrayListOf<String>()

        for(i in courses)
            if(i.isNotEmpty()) array.add(i)

        return ListString(array)
    }
}