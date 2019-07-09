package com.example.beta.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.data.ExampleCourses
import com.example.beta.R
import com.example.beta.adapters.CoursesRecyclerAdapter
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.view_model.CoursesViewModel

import kotlinx.android.synthetic.main.fragment_courses_table.*

class CoursesTableFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel
    private var category: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_courses_table, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments?.getString("name")

        fragment_courses_table_categorie.text = category

        val adapter = CoursesRecyclerAdapter(context!!)
        fragment_courses_table_list.layoutManager = LinearLayoutManager(context)
        fragment_courses_table_list.adapter = adapter

        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        coursesViewModel.allCourses.observe(this, Observer {

            adapter.setCourses(getlistByCtg(it))
        })
    }

    fun getlistByCtg(list: List<CoursesEnt>): List<CoursesEnt>{

        val nArray = arrayListOf<CoursesEnt>()

        for(i in list){

            if(i.category == category) nArray.add(i)
        }

        return nArray
    }
}
