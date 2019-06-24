package com.example.beta.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.CategoriesRecyclerAdapter
import com.example.beta.data.CategoriesListing
import kotlinx.android.synthetic.main.fragment_courses_menu.*

class CoursesMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_courses_menu, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfCategories = ArrayList<CategoriesListing>()
        listOfCategories.add(CategoriesListing(R.drawable.account_group, "Populares"))
        listOfCategories.add(CategoriesListing(R.drawable.new_box, "Novos"))
        listOfCategories.add(CategoriesListing(R.drawable.ic_baseline_nature_24px, "Natureza"))
        listOfCategories.add(CategoriesListing(R.drawable.bank_outline, "Cultural"))
        listOfCategories.add(CategoriesListing(R.drawable.school, "Educacional"))
        listOfCategories.add(CategoriesListing(R.drawable.food, "Gastronomia"))
        listOfCategories.add(CategoriesListing(R.drawable.movie, "Entretenimento"))

        /*val dividerItem = DividerItemDecoration(fragment_courses_menu_list.context, DividerItemDecoration.VERTICAL)
        dividerItem.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider, null))
        fragment_courses_menu_list.addItemDecoration(dividerItem)*/

        fragment_courses_menu_list.layoutManager = LinearLayoutManager(context)
        fragment_courses_menu_list.adapter = CategoriesRecyclerAdapter(context!!, listOfCategories)
    }
}
