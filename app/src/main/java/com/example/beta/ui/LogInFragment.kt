package com.example.beta.ui

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.beta.R

class LogInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    //Tentar ver problemas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.fragment_login_button).setOnClickListener {

            val usernameEmpty = view.findViewById<EditText>(R.id.fragment_login_username).text.isEmpty()
            val passEmpty = view.findViewById<EditText>(R.id.fragment_login_password).text.isEmpty()

            if (usernameEmpty || passEmpty) {

                    Toast.makeText(context, "Missing username or password", Toast.LENGTH_SHORT).show()


            } else {
                findNavController(this).navigate(R.id.coursesMenuFragment)
            }
        }

        view.findViewById<View>(R.id.fragment_login_register).setOnClickListener {

            findNavController(this).navigate(R.id.registerFragment)
        }
    }
}
