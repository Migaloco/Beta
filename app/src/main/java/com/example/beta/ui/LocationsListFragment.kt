package com.example.beta.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.LocationsRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_locations_list.*

class LocationsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activities = arguments?.getStringArrayList("descriptions")!!

        fragment_locations_list_list.layoutManager = LinearLayoutManager(context)
        fragment_locations_list_list.adapter = LocationsRecyclerAdapter(context!!, activities)
    }


    override fun onPrepareOptionsMenu(menu: Menu){

        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}
