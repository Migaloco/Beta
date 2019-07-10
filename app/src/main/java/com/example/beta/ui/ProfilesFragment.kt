package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.KeyListener
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beta.R
import com.example.beta.database.entities.UsersEntity
import com.example.beta.others.HttpRequest
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class ProfilesFragment : Fragment() {

    private var mUpdateUser: AsyncTaskCheckUpdateUsersProf? = null
    private var mUpdateRestUser: AsyncTaskRestOfUserInfo? = null
    private lateinit var district: JSONObject
    private var method: String? = null
    private var url: String? = null
    private var email:String? = null
    private var phone: Int? = null
    private var username: String? = null
    private var points: Int = 0
    private var toDo: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        district = JSONObject()

        getUserInfo()

        getRestOfUserInfo()

        fragment_profiles_courses_done_arrow.setOnClickListener {

        }

        fragment_profiles_info_pessoal_arrow.setOnClickListener {
            
            val bundle = Bundle()

            bundle.putString("email", email)
            bundle.putInt("phone", phone!!)

            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_personalInfo, bundle)
        }

        val pString = "$points points"
        fragment_profiles_points_text.text = pString
    }

    fun getUserInfo(){

        url = "https://turisnova.appspot.com/rest/listUsers/UserSelect"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        username = settings.getString("username", null)

        district.accumulate("username", username)

        mUpdateUser = AsyncTaskCheckUpdateUsersProf()
        mUpdateUser!!.execute(null)
        val result = mUpdateUser!!.get()

        val code = result.get(0)

        when(code){
            "200" ->{
                val arrayJ = JSONArray(result[1])

                val propMap = arrayJ.getJSONObject(0).getJSONObject("propertyMap")

                email = propMap.getString("user_email")
                phone = propMap.getInt("user_telemovel")
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskCheckUpdateUsersProf internal constructor() :
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
            mUpdateUser = null
        }

        override fun onCancelled() {
            mUpdateUser = null
        }
    }

    fun getRestOfUserInfo(){

        url = "https://turisnova.appspot.com/rest/UserRoute/getRouteStats"
        method = "GET"

        district = JSONObject()

        mUpdateRestUser = AsyncTaskRestOfUserInfo()
        mUpdateRestUser!!.execute(null)
        val result = mUpdateRestUser!!.get()

        when(result.get(0)){
            "200" ->{
                val arrayJ = JSONArray(result[1])

                for(i in 0 until arrayJ.length()){

                    val propMap = arrayJ.getJSONObject(i).getJSONObject("propertyMap")
                    val name = propMap.getString("username_stats")

                    if(name == username) {

                        val points = propMap.getInt("points")
                        val course = propMap.getString("route_title")

                        this.points += points
                        toDo.add(course)
                    }
                }
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskRestOfUserInfo internal constructor() :
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
            mUpdateRestUser = null
        }

        override fun onCancelled() {
            mUpdateRestUser = null
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}