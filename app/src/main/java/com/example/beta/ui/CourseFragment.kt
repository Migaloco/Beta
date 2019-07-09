package com.example.beta.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.CoursePhotosRecyclerAdapter
import com.example.beta.database.RoomDatabaseApp
import com.example.beta.database.converter.ListString
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.view_model.CoursesViewModel
import com.google.gson.Gson
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.beta.database.dao.CategoriesDao
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.ConverterForUI
import kotlinx.android.synthetic.main.fragment_course.*

class CourseFragment : Fragment() {

    private lateinit var viewModel: CoursesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var coursentity:CoursesEnt? = null

        viewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        val course = arguments?.getString("course")!!
        val location = arguments?.getString("location")!!
        val difficulty = arguments?.getDouble("difficulty")!!
        val category = arguments?.getString("category")!!
        val description = arguments?.getString("description")!!
        val photos = ConverterForUI().listStringToListInt(arguments?.getStringArrayList("photos")!!)
        val activities = arguments?.getStringArrayList("activities")!!

        viewModel.getCourse(course)

        viewModel.singleCourse.observe(this, Observer {

            coursentity = it
        })

        val adapter = CoursePhotosRecyclerAdapter(context!!, photos)
        fragment_course_photos.adapter = adapter
        fragment_course_photos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        fragment_course_name.text = course
        fragment_course_difficulty.text = "Difficulty: ${String.format("%f.1",difficulty)}"
        fragment_course_catefory1.text = category
        fragment_course_location.text = "Location: $location"
        fragment_course_description.text = description
        /*fragment_courses_activities.setOnClickListener {

            val bundle = Bundle()
            bundle.putStringArrayList("activities", activities)
            Navigation.findNavController(view).navigate(R.id.action_courseFragment_to_locationsListFragment, bundle)
        }*/
    }
}