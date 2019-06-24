package com.example.beta.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.beta.R

class CommentsFragmment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments_fragmment, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}
