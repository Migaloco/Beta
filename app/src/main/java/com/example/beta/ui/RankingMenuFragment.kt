package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
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
import com.example.beta.data.RankingData
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

    lateinit var listFromServ: ArrayList<RankingData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        district = JSONObject()

        val arrayServ = getUsers()

        val ordered = orderRanking(arrayServ)

        fragment_ranking_menu_list.layoutManager = LinearLayoutManager(context)
        fragment_ranking_menu_list.adapter = RankingRecyclerAdapter(context!!, ordered)

    }

    fun orderRanking(arrayServ: ArrayList<RankingData>):ArrayList<RankingData>{

        val array = arrayListOf<RankingData>()

        for(i in arrayServ){

            if(array.size == 0){
                array.add(i)
            }else {
                val points = i.points

                for (o in 0 until array.size) {

                    if (points > array[o].points || o == array.size - 1){
                        array.add(o,i)
                        break
                    }
                }
            }
        }

        return array
    }

    fun getUsers(): ArrayList<RankingData>{

        url = "https://turisnova.appspot.com/rest/UserRoute/getRouteStats"
        method = "GET"

        mUpdateUsers = AsyncTaskCheckUpdateUsers()
        mUpdateUsers!!.execute(null)
        val result = mUpdateUsers!!.get()

        val code = result.get(0)

        val ranking: ArrayList<RankingData> = arrayListOf()

        when(code){
            "200" ->{
                val arrayJ = JSONObject(result[1])
                val names = arrayJ.keys()

                names.forEach {

                    val points = arrayJ.getInt(it)

                    ranking.add(RankingData(it,points))
                }
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
        }

        return ranking
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
            return HttpRequest().doHTTP(URL(url), district, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mUpdateUsers = null
        }

        override fun onCancelled() {
            mUpdateUsers = null
        }
    }
}