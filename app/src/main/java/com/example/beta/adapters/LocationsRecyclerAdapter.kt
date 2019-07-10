package com.example.beta.adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.list_locations_layout.view.*

class LocationsRecyclerAdapter (val context: Context, val location: ArrayList<String>):
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

        Log.d(TAG, "${item}")

        holder.view.fragment_list_locations_layout_name.text = item
    }
}