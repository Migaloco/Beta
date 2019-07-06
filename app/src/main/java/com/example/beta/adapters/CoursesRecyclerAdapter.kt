package com.example.beta.adapters

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.others.CustomViewHolder
import com.google.gson.Gson
import com.example.beta.others.ConverterForUI
import kotlinx.android.synthetic.main.courses_table_list_layout.view.*


class CoursesRecyclerAdapter (val context: Context):
    RecyclerView.Adapter<CustomViewHolder>() {

    private var course: List<CoursesEnt> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val cellForRow = layoutInflater.inflate(R.layout.courses_table_list_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return course.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val item = course[position]

        val hold = holder.view

        hold.courses_table_list_layout_name.text = item.course
        hold.courses_table_list_layout_difficulty.text = ": " + item.difficulty
        hold.courses_table_list_layout_distance.text = ": " + item.distance + "km"
        hold.courses_table_list_layout_district.text = item.location

        val dividerItem = DividerItemDecoration(hold.courses_table_list_layout_photo_list.context, DividerItemDecoration.VERTICAL)
        dividerItem.setDrawable(hold.resources.getDrawable(R.drawable.recycler_view_divider, null))
        hold.courses_table_list_layout_photo_list.addItemDecoration(dividerItem)

        hold.courses_table_list_layout_photo_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hold.courses_table_list_layout_photo_list.adapter = CoursePhotosRecyclerAdapter(context, item.photos.list)

        hold.courses_table_list_layout_all.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("course", item.course)
            bundle.putString("location", item.location)
            bundle.putDouble("difficulty", item.difficulty)
            bundle.putDouble("distance", item.distance)
            bundle.putString("category",item.category)
            bundle.putString("description", item.description)
            bundle.putStringArrayList("photos", ConverterForUI().listIntToArrayListString(item.photos))
            bundle.putStringArrayList("activities", ConverterForUI().listStringToArrayListString(item.activities))

            Navigation.findNavController(hold).navigate(R.id.action_coursesTableFragment_to_courseFragment, bundle)
        }
    }

    internal fun setCourses(course: List<CoursesEnt>){
        this.course = course
        notifyDataSetChanged()
    }
}