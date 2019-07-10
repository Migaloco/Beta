package com.example.beta.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.LocationsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_list_routes_done.*

class ListRoutesDone : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_routes_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toDo = arguments?.getStringArrayList("todo")

        fragment_list_routes_list.layoutManager = LinearLayoutManager(context)
        fragment_list_routes_list.adapter = LocationsRecyclerAdapter(context!!, toDo!!)
    }
}
