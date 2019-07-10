package com.example.beta.others

import com.google.android.gms.maps.model.LatLng


class StringToDouble(){

    fun getStringToListDoubles(srt: String): List<LatLng>{

        val photos = srt.split(",")
        var array: ArrayList<Double> = arrayListOf()

        for(i in photos){

            if(i.isNotEmpty())array.add(i.toDouble())
        }

        val listLt: ArrayList<LatLng> = arrayListOf()

        var x = 0
        var y = 1
        while(y < array.size){

            listLt.add(LatLng(array[x], array[y]))
            x += 2
            y += 2
        }

        return listLt
    }
}