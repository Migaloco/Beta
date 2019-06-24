package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.CoursesListing
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.course_photos_layout.view.*


class CoursePhotosRecyclerAdapter (val context: Context, val photos: List<Int>):
    RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.course_photos_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val item = photos[position]

        holder.view.course_photos_layout_photo.setImageResource(item)
    }
}