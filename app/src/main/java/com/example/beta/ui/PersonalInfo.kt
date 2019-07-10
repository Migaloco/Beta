package com.example.beta.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.method.KeyListener
import android.text.style.BackgroundColorSpan
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.beta.R
import com.example.beta.others.HttpRequest
import com.google.android.gms.common.api.internal.BackgroundDetector
import kotlinx.android.synthetic.main.fragment_personal_info.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class PersonalInfo : Fragment() {

    private var mUpdateUser: AsyncTaskCheckUpdateUsersProf? = null
    private lateinit var json: JSONObject
    private var method: String? = null
    private var url: String = ""

    private var save :MenuItem? = null
    private var edit :MenuItem? = null

    var keyListenerArrayList: ArrayList<KeyListener> = arrayListOf()
    var background:ArrayList<Drawable> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateArrayType()

        setDisabled()

        getUserInfo()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getUserInfo(){

        url = "https://turisnova.appspot.com/rest/listUsers/UserSelect"
        method = "POST"

        val settings = context!!.getSharedPreferences("AUTHENTICATION", 0)
        val username = settings.getString("username", null)

        json = JSONObject()
        json.accumulate("username", username)

        mUpdateUser = AsyncTaskCheckUpdateUsersProf()
        mUpdateUser!!.execute(null)
        val result = mUpdateUser!!.get()

        val code = result.get(0)

        when(code){
            "200" ->{
                val arrayJ = JSONArray(result[1])

                val propMap = arrayJ.getJSONObject(0).getJSONObject("propertyMap")

                val email = propMap.getString("user_email")
                val phone = propMap.getInt("user_telemovel").toString()

                fragment_profiles_email_text.setText(email, TextView.BufferType.EDITABLE)
                fragment_profiles_phone_text.setText(phone, TextView.BufferType.EDITABLE)
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
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun populateArrayType(){

        keyListenerArrayList.add(fragment_profiles_email_text.keyListener)
        keyListenerArrayList.add(fragment_profiles_phone_text.keyListener)
        background.add(fragment_profiles_email_text.background)
        background.add(fragment_profiles_phone_text.background)
    }

    fun changeInfo(){

        val bundle = Bundle()

        bundle.putString("email", fragment_profiles_email_text.text.toString())
        bundle.putInt("phone", fragment_profiles_phone_text.text.toString().toInt())

        Navigation.findNavController(view!!).navigate(R.id.action_personalInfo_to_confirmChangesFragment, bundle)

        setDisabled()
    }

    fun setEditable(){

        enableEditText(fragment_profiles_email_text, 0)
        enableEditText(fragment_profiles_phone_text, 1)
    }


    fun setDisabled(){

        disableEditText(fragment_profiles_email_text, false)
        disableEditText(fragment_profiles_phone_text, true)
    }

    private fun disableEditText(editText: EditText, bol:Boolean){

        editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun enableEditText(editText: EditText, pos:Int){

        editText.keyListener = keyListenerArrayList[pos]
        editText.background = background[pos]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.profiles_menu_edit -> {
                edit!!.isVisible = false
                save!!.isVisible = true
                setEditable()
            }

            R.id.profiles_menu_save -> {
                edit!!.isVisible = true
                save!!.isVisible = false
                changeInfo()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.profiles_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu){

        save = menu.findItem(R.id.profiles_menu_save)
        edit = menu.findItem(R.id.profiles_menu_edit)

        save?.isVisible = false

    }
}
