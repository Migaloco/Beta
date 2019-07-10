package com.example.beta.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.method.KeyListener
import android.text.style.BackgroundColorSpan
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.beta.R
import com.google.android.gms.common.api.internal.BackgroundDetector
import kotlinx.android.synthetic.main.fragment_personal_info.*


class PersonalInfo : Fragment() {

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

        val email = arguments?.getString("email")!!
        val phone = "${arguments?.getInt("phone")!!}"


        fragment_profiles_email_text.setText(email, TextView.BufferType.EDITABLE)
        fragment_profiles_phone_text.setText(phone, TextView.BufferType.EDITABLE)

        populateArrayType()

        setDisabled()
    }

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
        val suggestions = menu.findItem(R.id.suggestionsFragment)
        val settings = menu.findItem(R.id.settingsFragment)

        save?.isVisible = false
        suggestions?.isVisible = false
        settings?.isVisible = false
    }
}
