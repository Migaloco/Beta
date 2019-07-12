package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.beta.R
import com.example.beta.data.CoursesData
import com.example.beta.data.DataCentral
import com.example.beta.database.converter.ListString
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.entities.UsersEntity
import com.example.beta.database.view_model.CoursesViewModel
import com.example.beta.database.view_model.UsersViewModel
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.HttpRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class WaitingFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var coursesViewModel: CoursesViewModel
    private lateinit var usersViewModel: UsersViewModel
    private var district: JSONObject? = null
    private var method: String? = null
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_waiting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        district = JSONObject()

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->

            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED ->{
                    NavHostFragment.findNavController(this).navigate(R.id.coursesMenuFragment)
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> checkToken()
                else -> {}
            }
        })
    }

    private fun checkToken(){

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)

        when(settings.getString("tokenID", null)){
            null -> NavHostFragment.findNavController(this).navigate(R.id.logInFragment)
            else -> {
                viewModel.authenticateWithToken()
                NavHostFragment.findNavController(this).navigate(R.id.coursesMenuFragment)
            }
        }
    }
}
