package com.example.beta.data

import com.example.beta.database.converter.ListString

data class CoursesData (val course: String, val district: String, val description: String
                       , val difficulty: Int, val waypoints: String, val wMarkers: ListString
                       , val wDescriptions: ListString, val start:String, val finish:String
                       , val category: String)