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
    //private var mUpdateTask: AsyncTaskCheckUpdate? = null
    //private var mUpdateUsers: AsyncTaskCheckUpdateUsers? = null
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

        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        district = JSONObject()

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->

            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED ->{
                    checkForUpdates()
                    showWelcomeMessage()
                    NavHostFragment.findNavController(this).navigate(R.id.coursesMenuFragment)
                }
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> checkToken()
                else -> {}
            }
        })
    }

    fun checkForUpdates(){

        //checkDistrict()
        //etDistrictCourses()
        //getUsers()
    }

    /*
    fun getDistrictCourses(){

        url = "https://turisnova.appspot.com/rest/getRoute/routeDistrict"
        method = "POST"

        mUpdateTask = AsyncTaskCheckUpdate()
        mUpdateTask!!.execute(null)
        val result = mUpdateTask!!.get()
        val code = result?.get(0)

        when(code){
            "200" -> {
                val list = JSONArray(result[1])
                val arrayCoursesEnt = arrayListOf<CoursesData>()

                for (i in 0 until list.length()){

                    val arrayM = arrayListOf<String>()
                    val arrayD = arrayListOf<String>()

                    val name = list.getJSONObject(i).getJSONObject("key").getString("name")
                    val mapProp = list.getJSONObject(i).getJSONObject("propertyMap")
                    val description = mapProp.getString("route_descricao")
                    val district = mapProp.getString("route_district")
                    val difficulty = mapProp.getInt("route_dificuldade")
                    val waypoints = mapProp.getString("route_waypoints")
                    val start = mapProp.getString("route_start")
                    val finish = mapProp.getString("route_finish")
                    val category = mapProp.getString("route_category")

                    val rWayMarker1 = mapProp.getString("route_coordMkr1")
                    arrayM.add(rWayMarker1)
                    val rWayMarker2 = mapProp.getString("route_coordMkr2")
                    arrayM.add(rWayMarker2)
                    val rWayMarker3 = mapProp.getString("route_coordMkr3")
                    arrayM.add(rWayMarker3)

                    val rWayDesc1 = mapProp.getString("route_marker1desc")
                    arrayD.add(rWayDesc1)
                    val rWayDesc2 = mapProp.getString("route_marker2desc")
                    arrayD.add(rWayDesc2)
                    val rWayDesc3 = mapProp.getString("route_marker3desc")
                    arrayD.add(rWayDesc3)

                    arrayCoursesEnt.add(CoursesData(name,district,description, difficulty ,waypoints, ListString(arrayM), ListString(arrayD), start, finish, category))
                }

                DataCentral().arrayCourses.addAll(arrayCoursesEnt)
                //coursesViewModel.insertAll(arrayCoursesEnt)
            }
            else -> Toast.makeText(context,"FailedUpdateCourses", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    fun getUsers(){

        url = "https://turisnova.appspot.com/rest/UserRoute/getRouteStats"
        method = "GET"

        mUpdateUsers = AsyncTaskCheckUpdateUsers()
        mUpdateUsers!!.execute(null)
        val result = mUpdateUsers!!.get()

        val code = result.get(0)

        when(code){
            "200" ->{
                val arrayJ = JSONArray(result[1])

                val mapCoursesEnt = mutableMapOf<String, UsersEntity>()

                for(i in 0 until arrayJ.length()){

                    val propMap = arrayJ.getJSONObject(i).getJSONObject("propertyMap")

                    val name = propMap.getString("username_stats")
                    if(!mapCoursesEnt.keys.contains(name)) mapCoursesEnt.put(name, UsersEntity(name))

                    val points = propMap.getInt("points")
                    val course = propMap.getString("route_title")

                    val user = mapCoursesEnt.get(name)!!

                    user.points += points
                    user.coursesDone.list.add(course)
                    //verificar se as alterações são feitas
                }

                //usersViewModel.insertAllUsers(ArrayList(mapCoursesEnt.values))
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
        }
    }*/

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
    }*/

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

    /*
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

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskCheckUpdateUsers internal constructor() :
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
    }*/
}
