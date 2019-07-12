package com.example.beta.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.beta.R
import android.net.NetworkCapabilities
import android.os.Build
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import com.example.beta.frag_view_model.LoginViewModel
import com.example.beta.others.IdCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_log_in.*
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

        val navController = findNavController(view)

        fragment_login_password_text.setBackgroundColor(Color.TRANSPARENT)

        view.findViewById<View>(R.id.fragment_login_button).setOnClickListener {

            val username = view.findViewById<EditText>(R.id.fragment_login_username).text
            val pass = view.findViewById<EditText>(R.id.fragment_login_password_text).text

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

    override fun onUserLogeedIn(token: JSONObject) {

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val editor = settings.edit()
        editor.putString("tokenID", token.getString("tokenID"))
        editor.putString("username", token.getString("username"))
        editor.apply()
    }

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

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val logout = menu.findItem(R.id.logout)

        logout.isVisible = false
    }
}
