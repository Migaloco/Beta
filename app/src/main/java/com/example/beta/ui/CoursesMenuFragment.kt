package com.example.beta.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.CategoriesRecyclerAdapter
import com.example.beta.data.CategoriesListing
import com.example.beta.database.view_model.CategoriesViewModel
import com.example.beta.frag_view_model.LoginViewModel
import kotlinx.android.synthetic.main.fragment_courses_menu.*

class CoursesMenuFragment : Fragment() {

    private lateinit var categoriesViewModel:CategoriesViewModel

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_courses_menu, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val dividerItem = DividerItemDecoration(fragment_courses_menu_list.context, DividerItemDecoration.VERTICAL)
        dividerItem.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider, null))
        fragment_courses_menu_list.addItemDecoration(dividerItem)*/

        val adapter = CategoriesRecyclerAdapter(context!!)

        fragment_courses_menu_list.layoutManager = LinearLayoutManager(context)
        fragment_courses_menu_list.adapter = adapter


        categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)

        categoriesViewModel.allCategories.observe(this, Observer {

            adapter.setCategoriesList(it)
        })

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->

            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> checkToken()
            }
        })
    }

    private fun showWelcomeMessage() {

        //Toast.makeText(context, "Welcome!", Toast.LENGTH_LONG).show()
    }

    private fun checkToken(){

        val navController = NavHostFragment.findNavController(this)
        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val token = settings.getString("tokenID", null)

        when(token){
            null -> navController.navigate(R.id.logInFragment)
            else -> {
                viewModel.authenticateWithToken()
                showWelcomeMessage()
            }
        }
    }
}