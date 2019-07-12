package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.data.ExampleCourses
import com.example.beta.R
import com.example.beta.adapters.CoursesRecyclerAdapter
import com.example.beta.data.CoursesData
import com.example.beta.data.DataCentral
import com.example.beta.database.converter.ListString
import com.example.beta.database.entities.CoursesEnt
import com.example.beta.database.entities.UsersEntity
import com.example.beta.database.view_model.CoursesViewModel
import com.example.beta.others.HttpRequest

import kotlinx.android.synthetic.main.fragment_courses_table.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class CoursesTableFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel
    private var category: String? = null
    lateinit var listFromServ: ArrayList<CoursesEnt>

    private var district: JSONObject? = null
    private var method: String? = null
    private var url: String? = null

    private var mUpdateTask: AsyncTaskCheckUpdate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_courses_table, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments?.getString("name")
        district = JSONObject()

        fragment_courses_table_categorie.text = category

        val adapter = CoursesRecyclerAdapter(context!!)
        fragment_courses_table_list.layoutManager = LinearLayoutManager(context)
        fragment_courses_table_list.adapter = adapter

        coursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)

        checkDistrict()
        getDistrictCourses()

        adapter.setCourses(getlistByCtg(listFromServ))
    }

    fun getlistByCtg(list: List<CoursesEnt>): List<CoursesEnt>{

        val nArray = arrayListOf<CoursesEnt>()

        for(i in list){
            if(i.category == category) nArray.add(i)
        }
        return nArray
    }

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
                val arrayCoursesEnt = arrayListOf<CoursesEnt>()

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

                    arrayCoursesEnt.add(CoursesEnt(name,district,description, difficulty ,waypoints, ListString(arrayM), ListString(arrayD), start, finish, category))
                }

                listFromServ = arrayCoursesEnt
            }
            else -> Toast.makeText(context,"FailedUpdateCourses", Toast.LENGTH_SHORT).show()
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
