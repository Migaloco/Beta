package com.example.beta.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.beta.R

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val password = view.findViewById<EditText>(R.id.fragment_register_password).text
        val confpass = view.findViewById<EditText>(R.id.fragment_register_conf_password).text

        view.findViewById<View>(R.id.fragment_register_button).setOnClickListener {

            if(view.findViewById<EditText>(R.id.fragment_register_username).text.isEmpty()
                || view.findViewById<EditText>(R.id.fragment_register_email).text.isEmpty()
                || password.isEmpty()
                || confpass.isEmpty()
                ){

                Toast.makeText(context, "Missing information", Toast.LENGTH_SHORT).show()

            }else if(password != confpass) Toast.makeText(context, "The passwords are different", Toast.LENGTH_SHORT).show()

            else findNavController(this).navigate(R.id.logInFragment)
        }
    }
}
