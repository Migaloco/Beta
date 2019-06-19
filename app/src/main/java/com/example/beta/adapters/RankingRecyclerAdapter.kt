package com.example.beta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta.R
import com.example.beta.others.CustomViewHolder
import kotlinx.android.synthetic.main.ranking_list_layout.view.*

class RankingRecyclerAdapter(val context: Context, val names: ArrayList<String>):RecyclerView.Adapter<CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.ranking_list_layout, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val getName = names.get(position)
        val rank = position + 1

        holder.view.ranking_list_layout_rank.text = rank.toString()
        holder.view.ranking_list_layout_name.text = getName
    }

}