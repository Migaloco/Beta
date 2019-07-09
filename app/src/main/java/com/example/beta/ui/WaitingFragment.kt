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
import androidx.navigation.fragment.NavHostFragment
import com.example.beta.R
import com.example.beta.database.view_model.CoursesViewModel
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.HttpRequest
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class WaitingFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var coursesViewModel: CoursesViewModel
    private var mUpdateTask: AsyncTaskCheckUpdate? = null
    private var district: JSONObject? = null
    private var method: String? = null
    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->

            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED ->{
                    //checkForUpdates()
                    showWelcomeMessage()
                    NavHostFragment.findNavController(this).navigate(R.id.coursesMenuFragment)
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> checkToken()
                else -> {}
            }
        })
    }

    fun checkForUpdates(){

        checkDistrict()
        getDistrictCourses()
    }

    fun getDistrictCourses(){

        url = "https://turisnova.appspot.com/rest/getRoute/routeDistrict"
        method = "POST"

        mUpdateTask = AsyncTaskCheckUpdate()
        mUpdateTask!!.execute(null)
        val r = mUpdateTask!!.get()
        val code = r?.get(0)

        when(code){
            "200" -> {
                val list = JSONArray(r[1])
                Log.d(TAG, "${r[1]}")
               //coursesViewModel.insertAll()
            }
        }
    }

    fun checkDistrict(){

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val districtSp = settings.getString("district", null)

        when(districtSp){
            null -> {
                settings.edit().putString("district", "Lisboa").apply()
                district!!.accumulate("district", "Lisboa")
            }
            else ->{
                district!!.accumulate("district", districtSp)
            }
        }
    }

    private fun showWelcomeMessage() {

        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show()
    }

    private fun checkToken(){

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)

        when(settings.getString("tokenID", null)){
            null -> NavHostFragment.findNavController(this).navigate(R.id.logInFragment)
            else -> {
                viewModel.authenticateWithToken()
                //checkForUpdates()
                showWelcomeMessage()
                NavHostFragment.findNavController(this).navigate(R.id.coursesMenuFragment)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskCheckUpdate internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {
            if (false) {
                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {
            return HttpRequest().doHTTP(URL(url), district!!, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mUpdateTask = null
        }

        override fun onCancelled() {
            mUpdateTask = null
        }
    }
}
