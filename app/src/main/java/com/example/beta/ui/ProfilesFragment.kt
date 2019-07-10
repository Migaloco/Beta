package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.method.KeyListener
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beta.R
import com.example.beta.database.entities.UsersEntity
import com.example.beta.others.HttpRequest
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beta.adapters.LocationsRecyclerAdapter
import kotlinx.android.synthetic.main.alert_window_password.*
import kotlinx.android.synthetic.main.alert_window_password.view.*
import kotlinx.android.synthetic.main.fragment_list_routes_done.*


class ProfilesFragment : Fragment() {


    private var mUpdateRestUser: AsyncTaskRestOfUserInfo? = null
    private var mDeleteUser: AsyncTaskDeleteUser? = null
    private lateinit var json: JSONObject
    private var method: String? = null
    private var url: String? = null
    private var username: String? = null
    private var points: Int? = null
    private var toDo: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        points = 0
        toDo = arrayListOf()

        json = JSONObject()

        getUserInfo()

        fragment_profiles_courses_done_arrow.setOnClickListener {

            val bundle = Bundle()
            bundle.putStringArrayList("todo", toDo)

            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_listRoutesDone, bundle)
        }

        fragment_profiles_info_pessoal_arrow.setOnClickListener {

            Navigation.findNavController(view).navigate(R.id.personalInfo)
        }

        val pString = "$points points"
        fragment_profiles_points_text.text = pString
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserInfo(){

        url = "https://turisnova.appspot.com/rest/UserRoute/getUserRoute"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        username = settings.getString("username", null)

        json = JSONObject()
        json.accumulate("username_stats", username)

        mUpdateRestUser = AsyncTaskRestOfUserInfo()
        mUpdateRestUser!!.execute(null)
        val result = mUpdateRestUser!!.get()

        when(result.get(0)){
            "200" ->{
                val arrayJ = JSONArray(result[1])

                val arrayL = arrayListOf<String>()

                for(i in 0 until arrayJ.length()){

                    val map = arrayJ.getJSONObject(i).getJSONObject("propertyMap")

                    val point = map.getInt("points")
                    val routeT = map.getString("route_title")

                    points = points!! + point
                    toDo!!.add(routeT)
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
            return HttpRequest().doHTTP(URL(url), json, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mUpdateRestUser = null
        }

        override fun onCancelled() {
            mUpdateRestUser = null
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteProfile(password: String) {

        url = "https://turisnova.appspot.com/rest/deleteAccount/user"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val username = settings.getString("username", null)!!

        json.accumulate("username", username)
        json.accumulate("password", password)

        mDeleteUser = AsyncTaskDeleteUser()
        mDeleteUser!!.execute()
        val result = mDeleteUser!!.get()
        val code = result[0]

        when (code) {
            "200" -> {
                Toast.makeText(context, "Succefully deleted account", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Goodbye", Toast.LENGTH_SHORT).show()
                appExit()
            }
        }
    }

    private fun appExit() {

        val editor = activity!!.getSharedPreferences("AUTHENTICATION", 0).edit()
        editor.clear()
        editor.apply()

        activity!!.finish()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun deleteDialog(){
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("Are you sure you want to delete your account?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                showPasswordDialog()
            }.setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    fun showPasswordDialog() {

        val dialogBuilder = AlertDialog.Builder(context!!)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_window_password, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle("Authentication")
        .setMessage("Please enter password")
        .setPositiveButton("Done") {dialog , _ ->

            val password = dialogView.dialog_password.text
            val confirmation = dialogView.dialog_password_conf.text

            if(password!!.isEmpty() || confirmation!!.isEmpty()) Toast.makeText(context, "Missing fields", Toast.LENGTH_SHORT).show()
            else if(password.toString() != confirmation.toString())Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            else{
                deleteProfile(password.toString())
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskDeleteUser internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {
            if (false) {
                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {
            return HttpRequest().doHTTP(URL(url), json, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mDeleteUser = null
        }

        override fun onCancelled() {
            mDeleteUser = null
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.delete_menu -> deleteDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_profile_menu, menu)
    }
}