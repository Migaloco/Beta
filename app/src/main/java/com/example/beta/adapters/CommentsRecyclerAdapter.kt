package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.data.CommentsData
import com.example.beta.database.entities.CategoriesEntity
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.comments_layout.view.*

class CommentsRecyclerAdapter (val context: Context): RecyclerView.Adapter<CustomViewHolder>(){

    var listOfComments: List<CommentsData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        val cellForRow = layoutInflater.inflate(R.layout.comments_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return listOfComments.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val hold = holder.view

        hold.comments_layout_text.text = listOfComments[position].comment
        hold.comments_layout_username.text = listOfComments[position].username
    }

    internal fun setListComments(list: List<CommentsData>){

        listOfComments = list
        notifyDataSetChanged()
    }
}