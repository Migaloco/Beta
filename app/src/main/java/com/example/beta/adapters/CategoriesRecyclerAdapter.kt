package com.example.beta.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.database.entities.CategoriesEntity
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.categories_list_layout.view.*

class CategoriesRecyclerAdapter(val context: Context): RecyclerView.Adapter<CustomViewHolder>(){

    var listOfCategories: List<CategoriesEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.categories_list_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return listOfCategories.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val getCategory = listOfCategories.get(position)

        holder.view.categories_list_layout_image.setImageResource(getCategory.icon)
        holder.view.categories_list_layout_text.text = getCategory.category
        holder.view.categories_list_layout_singleton.setOnClickListener {

            val category = getCategory.category

            var bundle = Bundle()
            bundle.putString("name", category)

            Navigation.findNavController(holder.view).navigate(R.id.action_coursesMenuFragment_to_coursesTableFragment, bundle)
        }
    }

    internal fun setCategoriesList(list: List<CategoriesEntity>){

        listOfCategories = list
        notifyDataSetChanged()
    }

}
