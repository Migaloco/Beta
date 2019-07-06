package com.example.beta.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.KeyListener
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beta.R
import kotlinx.android.synthetic.main.fragment_profiles.*

class ProfilesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fragment_profiles_courses_done_arrow.setOnClickListener {


        }

        fragment_profiles_info_pessoal_arrow.setOnClickListener {

            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_personalInfo)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        val search = menu.findItem(R.id.search_vie)
        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        search?.isVisible = false
        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}
