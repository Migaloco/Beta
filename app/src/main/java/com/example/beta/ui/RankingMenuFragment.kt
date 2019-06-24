package com.example.beta.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.RankingRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_ranking_menu.*

class RankingMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rankingList = ArrayList<String>()

        rankingList.add("Miguel")
        rankingList.add("Ricardo")
        rankingList.add("Madeira")
        rankingList.add("Jhordy")
        rankingList.add("Joao")
        rankingList.add("Joana")
        rankingList.add("Barbara")
        rankingList.add("Gonçalo")
        rankingList.add("Nuno")
        rankingList.add("Henrique")
        rankingList.add("David")

        fragment_ranking_menu_list.layoutManager = LinearLayoutManager(context)
        fragment_ranking_menu_list.adapter = RankingRecyclerAdapter(context!!, rankingList)

    }
}