package com.example.beta.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.method.KeyListener
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import com.example.beta.R
import kotlinx.android.synthetic.main.fragment_personal_info.*


class PersonalInfo : Fragment() {

    private var save :MenuItem? = null
    private var edit :MenuItem? = null

    var keyListenerArrayList: ArrayList<KeyListener> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        populateArrayType()

        setDisabled()

        super.onViewCreated(view, savedInstanceState)
    }

    fun populateArrayType(){

        keyListenerArrayList.add(fragment_profiles_username_text.keyListener)
        keyListenerArrayList.add(fragment_profiles_email_text.keyListener)
        keyListenerArrayList.add(fragment_profiles_phone_text.keyListener)
        keyListenerArrayList.add(fragment_profiles_passwrod_text.keyListener)
    }

    fun changeInfo(){
        setDisabled()
    }

    fun setEditable(){

        enableEditText(fragment_profiles_username_text, 0)
        enableEditText(fragment_profiles_email_text, 1)
        enableEditText(fragment_profiles_phone_text, 2)
        enableEditText(fragment_profiles_passwrod_text, 3)
    }


    fun setDisabled(){

        disableEditText(fragment_profiles_username_text, false)
        disableEditText(fragment_profiles_email_text, false)
        disableEditText(fragment_profiles_phone_text, false)
        disableEditText(fragment_profiles_passwrod_text, true)
    }

    private fun disableEditText(editText: EditText, bol:Boolean){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.focusable = View.NOT_FOCUSABLE
        }
        editText.keyListener = null
        editText.setBackgroundColor(Color.TRANSPARENT)

        //if(bol) fragment_profiles_passwrod_text_layout.isEnabled = false    No caso de querer fazer de outra forma
    }

    private fun enableEditText(editText: EditText, pos:Int){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.focusable = View.FOCUSABLE
        }
        editText.keyListener = keyListenerArrayList[pos]
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
        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        save?.isVisible = false
        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}
