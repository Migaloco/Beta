package com.example.beta.frag_view_model

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beta.others.HttpRequest
import com.example.beta.ui.RegisterFragment
import com.google.gson.JsonObject
import org.json.JSONObject
import java.net.URL

class RegisterViewModel : ViewModel() {

    enum class RegistrationState {
        COLLECT_PROFILE_DATA,
        COLLECT_USER_PASSWORD,
        REGISTRATION_COMPLETED,
        REGISTRATION_FAILED
    }

    var mAuthTask: AsyncTaskSignIn? = null

    val registrationState = MutableLiveData<RegistrationState>(RegistrationState.COLLECT_PROFILE_DATA)
    var user : JSONObject? = null
        private set

    init {
        user = JSONObject()
    }

    fun collectProfileData(email: String, telefone: String) {

        user!!.accumulate("mail", email)
        user!!.accumulate("telemovel", telefone)

        // Change State to collecting username and password
        registrationState.value = RegistrationState.COLLECT_USER_PASSWORD
    }

    fun createAccountAndLogin(username: String, password: String, confPass:String) {

        user!!.accumulate("username", username)
        user!!.accumulate("password", password)
        user!!.accumulate("confirmation", confPass)

        if (mAuthTask != null) {
            return
        }

        mAuthTask = AsyncTaskSignIn()
        mAuthTask!!.execute(null)

        val res = mAuthTask!!.get()

        when(res) {
            "200" -> registrationState.value = RegistrationState.REGISTRATION_COMPLETED
            else -> registrationState.value = RegistrationState.REGISTRATION_FAILED
        }
    }

    fun userCancelledRegistration() : Boolean {

        user = JSONObject()

        registrationState.value = RegistrationState.COLLECT_PROFILE_DATA
        return true
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskSignIn internal constructor() :
        AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {

            if (false) {

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
}