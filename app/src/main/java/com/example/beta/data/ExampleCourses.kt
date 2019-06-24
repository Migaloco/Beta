package com.example.beta.data

import com.example.beta.R

class ExampleCourses (){

    fun getExamples():ArrayList<CoursesListing>{

        val array = arrayListOf<CoursesListing>()

        val photos = getPhotos()

        array.add(
            CoursesListing("Midnight Fun", "Lisboa", "Neste percurso vamos explorar o lado de Lisboa"
            , 2.0, 1.4, photos[0]))

        array.add(
            CoursesListing("Explorar o Porto", "Porto", "Com este percurso o vai ficar a conhecer um lado"
            , 1.6, 2.0, photos[1]))

        array.add(
            CoursesListing("Recantos de Coimbra", "Coimbra", "Estás pronto para percorrer uma das cidades"
            , 3.0, 2.1, photos[2]))

        array.add(
            CoursesListing("Prazer do Algarve", "Faro", "As praias do algarve escondem maravilhas rochosas"
            , 2.5, 4.6, photos[3]))

        array.add(
            CoursesListing("Descansar a Alentejano", "Beja", "Se estás farto da confusão da cidade, este é"
                , 1.4, 4.0, photos[4]))

        array.add(
            CoursesListing("Conhecer Monsanto", "Lisboa", "Nada como estar numa floresta para fugir ao sufoco da cidade"
                ,3.0, 3.7, photos[5])
        )

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
}