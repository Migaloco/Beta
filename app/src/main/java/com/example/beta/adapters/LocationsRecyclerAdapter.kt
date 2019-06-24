package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.LocationListing
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.list_locations_layout.view.*

class LocationsRecyclerAdapter (val context: Context, val location: ArrayList<LocationListing>):
    RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_locations_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return location.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val item = location[position]

        holder.view.fragment_list_locations_layout_photo.setImageResource(item.photo)
        holder.view.fragment_list_locations_layout_name.text = item.name
    }
}