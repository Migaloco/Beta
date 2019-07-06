package com.example.beta.frag_view_model

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.others.HttpRequest
import com.example.beta.ui.LogInFragment
import org.json.JSONObject
import java.net.URL

class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED,          // Initial state, the user needs to authenticate
        UNAUTHENTICATED,        // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    var token: JSONObject
    var mLogInTask: AsyncTaskLogIn? = null
    var user : JSONObject? = null

    init {
        // In this example, the user is always unauthenticated when MainActivity is launched
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        token = JSONObject()
        user = JSONObject()
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authenticate(username: String, password: String) {
        if (passwordIsValidForUsername(username, password)) {
            authenticationState.value =
                AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value =
                AuthenticationState.INVALID_AUTHENTICATION
        }
    }

    private fun passwordIsValidForUsername(username: String, password: String): Boolean {

        user!!.accumulate("username", username)
        user!!.accumulate("password", password)

        mLogInTask = AsyncTaskLogIn()
        mLogInTask!!.execute(null)
        val r = mLogInTask!!.get()?.get(0)

        return when(r){
            "200" -> true
            else -> false
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskLogIn internal constructor() :
        AsyncTask<Void, Void, List<String>>() {

        override fun onPreExecute() {

            if (false) {

                cancel(true)
            }
        }

        override fun doInBackground(vararg params: Void): List<String>? {

            return HttpRequest().doHTTP(URL("https://turisnova.appspot.com/rest/login/user"), user!!, "POST")
        }

        override fun onPostExecute(success: List<String>?) {

            mLogInTask = null

            if (success != null) {

                token = JSONObject(success[1])
            }
        }

        override fun onCancelled() {
            mLogInTask = null
        }
    }
}