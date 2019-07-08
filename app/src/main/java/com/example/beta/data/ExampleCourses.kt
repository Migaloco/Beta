package com.example.beta.data

import com.example.beta.R
import com.example.beta.database.converter.ListInt
import com.example.beta.database.converter.ListString
import com.example.beta.database.entities.CategoriesEntity
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.entities.LocationsEntity
import com.example.beta.database.entities.UsersEntity
import com.google.android.gms.maps.model.LatLng

class ExampleCourses (){


    /*
    Examples for the Courses
     */
    fun getExamplesCourses():List<CoursesEnt>{

        val array = arrayListOf<CoursesEnt>()

        val photos = getPhotos()

        array.add(
            CoursesEnt(
                "fun", "Lisboa", "Neste percurso vamos explorar o lado de Lisboa"
                , 2.0, 1.4, ListInt(photos[0]), "Natureza", ListString(listOf("Pavilhao do Conhecimento", "Chiado"))
            ))

        array.add(
            CoursesEnt(
                "porto", "Porto", "Com este percurso o vai ficar a conhecer um lado"
                , 1.6, 2.0, ListInt(photos[1]), "Gastronomia", ListString(listOf("Oceanario", "Praça do Comercio"))))
        /*
        array.add(
            CoursesEnt(
                "Recantos de Coimbra", "Coimbra", "Estás pronto para percorrer uma das cidades"
                , 3.0, 2.1,  ListInt(photos[2]), "Cultural", ListString(listOf("Praça do Comercio", "Chiado"))
            ))

        array.add(
            CoursesEnt(
                "Prazer do Algarve", "Faro", "As praias do algarve escondem maravilhas rochosas"
                , 2.5, 4.6, ListInt(photos[3]), "Natureza", ListString(listOf("Praça do Comercio", "Pavilhao do Conhecimento"))))

        array.add(
            CoursesEnt(
                "Descansar a Alentejano", "Beja", "Se estás farto da confusão da cidade, este é"
                , 1.4, 4.0, ListInt(photos[4]), "Cultural", ListString(listOf("Chiado", "Oceanario"))
            ))

        array.add(
            CoursesEnt(
                "Conhecer Monsanto", "Lisboa", "Nada como estar numa floresta para fugir ao sufoco da cidade"
                , 3.0, 3.7, ListInt(photos[5]), "Cultural", ListString(listOf("Pavilhao do Conhecimento", "Oceanario"))))*/
        return array
    }

    private fun getPhotos(): List<List<Int>>{

        val lisboa = listOf<Int>(
            R.drawable.lisboa_1,
            R.drawable.lisboa_praca)

        val porto = listOf<Int>(
            R.drawable.porto_1,
            R.drawable.porto_cidade)

        val coimbra = listOf<Int>(
            R.drawable.coimbra_1,
            R.drawable.coimbra_universidade)

        val algarve = listOf<Int>(
            R.drawable.algarve_1,
            R.drawable.algarve_praia)

        val alentejo = listOf<Int>(
            R.drawable.alentejo_1,
            R.drawable.alentejo_campo)

        val monsanto = listOf<Int>(
            R.drawable.monsanto,
            R.drawable.monsanto1,
            R.drawable.monsanto_2)

        return listOf(lisboa, porto, coimbra, algarve, alentejo, monsanto)
    }

    /*
    Returns examples of locations to use on app
     */
    fun getExamplesLocations():List<LocationsEntity>{

        val arrayList = arrayListOf<LocationsEntity>()

        val array = getPhotos()

        arrayList.add(
            LocationsEntity("Pavilhao do Conhecimento", "Here you will explore how fun science can be"
            , array[0][0]))

        arrayList.add(
            LocationsEntity("Praça do Comercio", "A beutiful place in downtown Lisbon that is a must see"
            ,array[1][0]))

        arrayList.add(
            LocationsEntity("Chiado", "A place filled with quality stores and restaurants"
            , array[0][1]))

        arrayList.add(
            LocationsEntity("Oceanario","Come see the fishes"
            , array[2][1]))

        return arrayList
    }


    /*
    Returns examples of users to use on app
     */
    fun getExamplesUsers():List<UsersEntity>{

        val arrayList = arrayListOf<UsersEntity>()

        arrayList.add(UsersEntity("Miguel", 400, ListString(emptyList()), ListString(emptyList())))
        arrayList.add(UsersEntity("Jhordy", 200, ListString(emptyList()), ListString(emptyList())))
        arrayList.add(UsersEntity("Ricardo", 200, ListString(emptyList()), ListString(emptyList())))
        arrayList.add(UsersEntity("Madeira", 100, ListString(emptyList()), ListString(emptyList())))

        return arrayList
    }

    /*
    Returns examples of cateogries to use on app
     */
    fun getExamplesCategories():List<CategoriesEntity>{

        val listOfCategories = arrayListOf<CategoriesEntity>()

        listOfCategories.add(CategoriesEntity("Natureza", R.drawable.ic_baseline_nature_24px))
        listOfCategories.add(CategoriesEntity("Cultural", R.drawable.bank_outline))
        listOfCategories.add(CategoriesEntity("Educacional", R.drawable.school))
        listOfCategories.add(CategoriesEntity("Gastronomia", R.drawable.food))
        listOfCategories.add(CategoriesEntity("Entretenimento", R.drawable.movie))

        return listOfCategories
    }

    fun getCourseCoordinates(): List<LatLng>{

        val list = arrayListOf<LatLng>()

        list.add(LatLng(38.715654, -9.237230))
        list.add(LatLng(38.718574, -9.240105))
        list.add(LatLng(38.709229, -9.254140))
        list.add(LatLng(38.705545, -9.254837))
        list.add(LatLng(38.700023, -9.253115))

        return list
    }
}