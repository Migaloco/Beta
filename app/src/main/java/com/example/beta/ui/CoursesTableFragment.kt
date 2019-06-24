package com.example.beta.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.data.ExampleCourses
import com.example.beta.R
import com.example.beta.adapters.CoursesRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_courses_menu.*

import kotlinx.android.synthetic.main.fragment_courses_table.*

class CoursesTableFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_courses_table, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_courses_table_categorie.text = arguments?.getString("name")

        val list = ExampleCourses().getExamples()


        fragment_courses_table_list.layoutManager = LinearLayoutManager(context)
        fragment_courses_table_list.adapter = CoursesRecyclerAdapter(context!!, list)
    }
}
