package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.CategoriesListing
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.categories_list_layout.view.*

class CategoriesRecyclerAdapter(val context: Context, val listOfCategories : ArrayList<CategoriesListing>): RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.categories_list_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val getCategory = listOfCategories.get(position)

        holder.view.categories_list_layout_image.setImageResource(getCategory.image)
        holder.view.categories_list_layout_text.text = getCategory.name
    }

}
