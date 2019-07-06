package com.example.beta.others

import com.example.beta.database.converter.ListInt
import com.example.beta.database.converter.ListString

class ConverterForUI {

    fun listIntToArrayListString(list:ListInt):ArrayList<String>{

        val array = arrayListOf<String>()

        for(i in list.list) array.add(i.toString())

        return array
    }

    fun listStringToListInt(list: ArrayList<String>):List<Int>{

        val array = arrayListOf<Int>()

        for(i in list) array.add(i.toInt())

        return array
    }

    fun listStringToArrayListString(list: ListString):ArrayList<String>{

        val array = arrayListOf<String>()

        array.addAll(list.list)

        return array
    }
}