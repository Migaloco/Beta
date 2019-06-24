package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.OtherInfo
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.course_list_layout.view.*

class OtherInfoRecyclerAdapter (val context: Context, val otherinfo: ArrayList<OtherInfo>):
    RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.course_list_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return otherinfo.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val item = otherinfo[position]

        holder.view.fragment_course_list_layout_icon.setImageResource(item.icon)
        holder.view.fragment_course_list_layout_text.text = item.title
        holder.view.fragment_course_list_layout_full.setOnClickListener {

            findNavController(holder.view).navigate(item.destFragment)
        }
    }
}