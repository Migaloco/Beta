package com.example.beta.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.beta.R
import com.example.beta.others.HttpRequest
import kotlinx.android.synthetic.main.fragment_confirm_changes.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class ConfirmChangesFragment : Fragment() {

    private lateinit var json: JSONObject
    private var method: String? = null
    private var url: String? = null
    private var mUpdateUserInfo: AsyncTaskUpdateUserInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_changes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        json = JSONObject()

        val email = arguments?.getString("email")!!
        val phone = arguments?.getInt("phone")!!

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val username = settings.getString("username", null)!!

        fragment_confirm_changes_submit.setOnClickListener {

            val pass = fragment_confirm_changes_password.text.toString()
            val confirm = fragment_confirm_changes_confirm.text.toString()

            updateUserInfo(username, email, phone, pass, confirm)
        }
    }

    fun updateUserInfo(username:String, email:String, phone:Int, pass:String, confirm:String){

        url = "https://turisnova.appspot.com/rest/changeData/update"
        method = "POST"

        json.accumulate("username", username)
        json.accumulate("mail", email)
        json.accumulate("telemovel", phone)
        json.accumulate("password", pass)
        json.accumulate("confirmation", confirm)

        mUpdateUserInfo = AsyncTaskUpdateUserInfo()
        mUpdateUserInfo!!.execute(null)
        val result = mUpdateUserInfo!!.get()

        val code = result.get(0)

        when(code){
            "200" ->{
                Toast.makeText(context,"Data updated succefully", Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(context,"FailedUpdateUsers", Toast.LENGTH_SHORT).show()
        }

        Navigation.findNavController(view!!).popBackStack(R.id.profileFragment, false)
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskUpdateUserInfo internal constructor() :
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
            mUpdateUserInfo = null
        }

        override fun onCancelled() {
            mUpdateUserInfo = null
        }
    }
}
