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
import kotlinx.android.synthetic.main.alert_window_password.*
import kotlinx.android.synthetic.main.alert_window_password.view.*


class ProfilesFragment : Fragment() {

    private var mUpdateUser: AsyncTaskCheckUpdateUsersProf? = null
    private var mUpdateRestUser: AsyncTaskRestOfUserInfo? = null
    private var mDeleteUser: AsyncTaskDeleteUser? = null
    private lateinit var json: JSONObject
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

        json = JSONObject()

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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserInfo(){

        url = "https://turisnova.appspot.com/rest/listUsers/UserSelect"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        username = settings.getString("username", null)

        json.accumulate("username", username)

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
            return HttpRequest().doHTTP(URL(url), json, method!!)
        }

        override fun onPostExecute(success: List<String>?) {
            mUpdateUser = null
        }

        override fun onCancelled() {
            mUpdateUser = null
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getRestOfUserInfo(){

        url = "https://turisnova.appspot.com/rest/UserRoute/getRouteStats"
        method = "GET"

        json = JSONObject()

        mUpdateRestUser = AsyncTaskRestOfUserInfo()
        mUpdateRestUser!!.execute(null)
        val result = mUpdateRestUser!!.get()
        val b = result.get(0)

        when(b){
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