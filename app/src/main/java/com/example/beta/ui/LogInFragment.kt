package com.example.beta.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.beta.R
import java.net.URL
import android.net.NetworkCapabilities
import android.os.Build
import android.util.JsonToken
import android.util.Log
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.HttpRequest
import com.example.beta.others.IdCallback
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject


class LogInFragment : Fragment(), IdCallback {

    //var mLogInTask: AsyncTaskLogIn? = null
    //var user : JSONObject? = null

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    //Tentar ver problemas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //user = JSONObject()

        val navController = findNavController(view)

        view.findViewById<View>(R.id.fragment_login_button).setOnClickListener {

            val username = view.findViewById<EditText>(R.id.fragment_login_username).text
            val pass = view.findViewById<EditText>(R.id.fragment_login_password).text

            if (username.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Missing username or password", Toast.LENGTH_SHORT).show()
            } else {

                viewModel.authenticate(username.toString(), pass.toString(), this)
            }
        }

        view.findViewById<View>(R.id.fragment_login_register).setOnClickListener {
            navController.navigate(R.id.otherRegisterInfoFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
        }

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> navController.popBackStack()
                LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION ->
                    Snackbar.make(view,"Wrong credentials", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)
        val logout = menu.findItem(R.id.logout)

        suggestions?.isVisible = false
        settings?.isVisible = false
        logout?.isVisible = false
    }

    override fun onUserLogeedIn(token: JSONObject) {

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val editor = settings.edit()
        editor.putString("tokenID", token.getString("tokenID"))
        editor.putString("username", token.getString("username"))
        editor.apply()
    }
/*
    private fun attemptLogIn() {

        if (mLogInTask != null) {
            return
        }

        mLogInTask = AsyncTaskLogIn()
        mLogInTask!!.execute(null)
        val r = mLogInTask!!.get()?.get(0)

        when (r) {
            "200" -> {
                Toast.makeText(context, "Successful Log in", Toast.LENGTH_SHORT).show()

                findNavController(this).navigate(R.id.coursesMenuFragment)
            }
            else -> {
                when (r) {
                    "403" -> Toast.makeText(context, "Invalid username and/or password", Toast.LENGTH_SHORT).show()
                    "500" -> Toast.makeText(context, "The server is fried", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //////////////////////////////////////////Internet///////////////////////////////////////////

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskLogIn internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {

            if (!isNetworkConnected()) {

                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {

               return HttpRequest().doHTTP(URL("https://turisnova.appspot.com/rest/login/user"), user!!, "POST")
        }

        override fun onPostExecute(success: List<String>?) {

            mLogInTask = null

            if (success != null) {
                val token: JSONObject?
                try {

                    token = JSONObject(success[1])
                    Log.i("LoginActivity", token.toString())

                    val settings = context?.getSharedPreferences("AUTHENTICATION", 0)
                    val editor = settings?.edit()
                    editor?.putString("tokenID", token.getString("tokenID"))

                    editor?.apply()

                } catch (e: JSONException) {

                    Log.e("Authentication", e.toString())
                }
            }
        }

        override fun onCancelled() {
            mLogInTask = null
        }
    }

*/
fun isNetworkConnected(): Boolean {

    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT < 23) {
        val ni = cm.activeNetworkInfo

        if (ni != null) {
            return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
        }
    } else {
        val n = cm.activeNetwork

        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)

            return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
    }
    return false
}
}
