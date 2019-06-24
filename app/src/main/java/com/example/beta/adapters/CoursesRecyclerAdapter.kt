package com.example.beta.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.CoursesListing
import com.example.beta.data.LocationListing
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.courses_table_list_layout.view.*
import kotlinx.android.synthetic.main.fragment_courses_menu.*
import kotlinx.android.synthetic.main.list_locations_layout.view.*

class CoursesRecyclerAdapter (val context: Context, val course: ArrayList<CoursesListing>):
    RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
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

        hold.courses_table_list_layout_name.text = item.name
        hold.courses_table_list_layout_difficulty.text = ": " + item.difficulty
        hold.courses_table_list_layout_distance.text = ": " + item.distance + "km"
        hold.courses_table_list_layout_district.text = item.location

        val dividerItem = DividerItemDecoration(hold.courses_table_list_layout_photo_list.context, DividerItemDecoration.VERTICAL)
        dividerItem.setDrawable(hold.resources.getDrawable(R.drawable.recycler_view_divider, null))
        hold.courses_table_list_layout_photo_list.addItemDecoration(dividerItem)

        hold.courses_table_list_layout_photo_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hold.courses_table_list_layout_photo_list.adapter = CoursePhotosRecyclerAdapter(context, item.photos)

        hold.courses_table_list_layout_all.setOnClickListener {


        }
    }
}