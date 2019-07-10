package com.example.beta.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.beta.R
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.HttpRequest
import androidx.activity.addCallback
import com.example.beta.frag_view_model.RegisterViewModel
import com.example.beta.others.RegisterCallback
import org.json.JSONObject
import java.net.URL

class RegisterFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(this)

        val username = view.findViewById<EditText>(R.id.fragment_register_username).text
        val password = view.findViewById<EditText>(R.id.fragment_register_password).text
        val confpass = view.findViewById<EditText>(R.id.fragment_register_conf_password).text

        view.findViewById<View>(R.id.fragment_register_button).setOnClickListener {

            if(username.isEmpty() || password.isEmpty() || confpass.isEmpty()){

                Toast.makeText(context, "Missing information", Toast.LENGTH_SHORT).show()

            }else if(password.toString() != confpass.toString()) Toast.makeText(context, "The passwords are different", Toast.LENGTH_SHORT).show()

            else{

                registrationViewModel.createAccountAndLogin(username.toString(), password.toString(), confpass.toString())
            }
        }

        registrationViewModel.registrationState.observe(
            viewLifecycleOwner, Observer { state ->
                if (state == RegisterViewModel.RegistrationState.REGISTRATION_COMPLETED) {

                    navController.popBackStack(R.id.logInFragment, false)
                    Toast.makeText(context, "Registration was a success", Toast.LENGTH_LONG).show()
                }
                if(state == RegisterViewModel.RegistrationState.REGISTRATION_FAILED){

                    Toast.makeText(context, "Registration failed", Toast.LENGTH_LONG).show()
                    navController.popBackStack(R.id.logInFragment, false)
                }
            }
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registrationViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.logInFragment, false)
        }
    }

    /*
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
    }*/

    ////////////////////////////////////Internet/////////////////////////////////////////////

    /*
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
    }*/

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
