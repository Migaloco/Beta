package com.example.beta.frag_view_model

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.others.HttpRequest
import com.example.beta.others.IdCallback
import org.json.JSONObject
import java.net.URL

class LoginViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED,          // Initial state, the user needs to authenticate
        UNAUTHENTICATED,        // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    var mLogInTask: AsyncTaskLogIn? = null
    var user : JSONObject
    private lateinit var IdCallback:IdCallback

    init {
        // In this example, the user is always unauthenticated when MainActivity is launched
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        user = JSONObject()
    }

    fun authenticateWithToken(){
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authenticate(username: String, password: String, idCallback: IdCallback) {
        if (passwordIsValidForUsername(username, password, idCallback)) {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }

    private fun passwordIsValidForUsername(username: String, password: String, idCallback: IdCallback): Boolean {

        user.accumulate("username", username)
        user.accumulate("password", password)

        mLogInTask = AsyncTaskLogIn()

        this.IdCallback = idCallback

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

            return HttpRequest().doHTTP(URL("https://turisnova.appspot.com/rest/login/user"), user, "POST")
        }

        override fun onPostExecute(success: List<String>?) {

            mLogInTask = null

            if (success != null) {

                val token = JSONObject(success[1])
                IdCallback.onUserLogeedIn(token)
            }
        }

        override fun onCancelled() {
            mLogInTask = null
        }
    }
}