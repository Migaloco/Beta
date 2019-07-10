package com.example.beta.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.R
import com.example.beta.adapters.RankingRecyclerAdapter
import com.example.beta.database.entities.UsersEntity
import com.example.beta.database.view_model.UsersViewModel
import com.example.beta.others.HttpRequest
import kotlinx.android.synthetic.main.fragment_ranking_menu.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class RankingMenuFragment : Fragment() {

    private lateinit var usersViewModel: UsersViewModel
    private var mUpdateUsers: AsyncTaskCheckUpdateUsers? = null
    private lateinit var district: JSONObject
    private var method: String? = null
    private var url: String? = null

    lateinit var rankingList: ArrayList<UsersEntity>
    lateinit var listFromServ: ArrayList<UsersEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        district = JSONObject()
        getUsers()

        rankingList = arrangeRankings()

        fragment_ranking_menu_list.layoutManager = LinearLayoutManager(context)
        fragment_ranking_menu_list.adapter = RankingRecyclerAdapter(context!!, rankingList)

    }

    fun arrangeRankings(): ArrayList<UsersEntity>{

        val arrayOrdered = arrayListOf<UsersEntity>()

        for(i in listFromServ){
            if(arrayOrdered.size == 0) arrayOrdered.add(i)
            else {
                val points = i.points
                val sizeA = arrayOrdered.size
                for (o in 0 until sizeA)
                    if(points > arrayOrdered[o].points || o + 1 == sizeA) arrayOrdered.add(o,i)
            }
        }
        return arrayOrdered
    }

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

                listFromServ = ArrayList(mapCoursesEnt.values)
                //usersViewModel.insertAllUsers(ArrayList(mapCoursesEnt.values))
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
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
            mUpdateUsers = null
        }

        override fun onCancelled() {
            mUpdateUsers = null
        }
    }
}