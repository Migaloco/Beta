package com.example.beta.ui

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.beta.R
import com.example.beta.others.HttpRequest
import org.json.JSONObject
import java.net.URL

class RegisterFragment : Fragment() {

    var mAuthTask: AsyncTaskSignIn? = null
    var user : JSONObject? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = JSONObject()

        var username = view.findViewById<EditText>(R.id.fragment_register_username).text
        var email = view.findViewById<EditText>(R.id.fragment_register_email).text
        var password = view.findViewById<EditText>(R.id.fragment_register_password).text
        var telefone = view.findViewById<EditText>(R.id.fragment_register_phone).text
        var confpass = view.findViewById<EditText>(R.id.fragment_register_conf_password).text

        view.findViewById<View>(R.id.fragment_register_button).setOnClickListener {

            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confpass.isEmpty()){

                Toast.makeText(context, "Missing information", Toast.LENGTH_SHORT).show()

            }else if(!password.toString().equals(confpass.toString())) Toast.makeText(context, "The passwords are different", Toast.LENGTH_SHORT).show()

            else{

                user!!.accumulate("username", username.toString())
                user!!.accumulate("password", password.toString())
                user!!.accumulate("mail", email.toString())
                user!!.accumulate("telemovel", telefone.toString())


                attemptSignIn()

                username.clear()
                email.clear()
                password.clear()
                telefone.clear()
                confpass.clear()
            }
        }
    }

    private fun attemptSignIn() {

        if (mAuthTask != null) {
            return
        }

        mAuthTask = AsyncTaskSignIn()
        mAuthTask!!.execute(null)

        val res = mAuthTask!!.get()

        when(res) {
            "200" -> {

                Toast.makeText(context, "Successful register", Toast.LENGTH_SHORT).show()

                findNavController(this).popBackStack()
            }
            else -> {
                when (res) {

                    "400" -> Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                    "500" -> Toast.makeText(context, "The server is fried", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        val search = menu.findItem(R.id.search_vie)
        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        search?.isVisible = false
        suggestions?.isVisible = false
        settings?.isVisible = false
    }

    ////////////////////////////////////Internet/////////////////////////////////////////////

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskSignIn internal constructor() :
        AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {

            if (!isNetworkConnected()) {

                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): String? {
            return try {

                HttpRequest().doHTTP(URL("https://turisnova.appspot.com/rest/register/user"), user!!, "POST")?.get(0)

            } catch (e: Exception) {
                e.toString()
            }
        }

        override fun onPostExecute(success: String?) {}

        override fun onCancelled() {
            mAuthTask = null
        }
    }

    fun isNetworkConnected(): Boolean {

        return HttpRequest().isNetworkConnected(context!!)
    }
}

class OtherRegisterInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_register_info, container, false)
    }
}
