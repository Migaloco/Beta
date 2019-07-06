package com.example.beta.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.beta.R
import androidx.activity.addCallback
import com.example.beta.frag_view_model.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_other_register_info.*

class OtherRegisterInfoFragment : Fragment() {

    val registrationViewModel by activityViewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_register_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(this)

        val email = view.findViewById<EditText>(R.id.fragment_other_register_info_email).text
        val role = view.findViewById<EditText>(R.id.fragment_other_register_info_role).text
        val telefone = view.findViewById<EditText>(R.id.fragment_other_register_info_phone).text

        fragment_other_register_info_button.setOnClickListener {

            if(email.isEmpty() || role.isEmpty()){

                Toast.makeText(context, "Missing information", Toast.LENGTH_SHORT).show()
            }else{

                registrationViewModel.collectProfileData(email.toString(), role.toString(), telefone.toString())
            }
        }

        registrationViewModel.registrationState.observe(
            viewLifecycleOwner, Observer { state ->
                if (state == RegisterViewModel.RegistrationState.COLLECT_USER_PASSWORD) {
                    navController.navigate(R.id.registerFragment)
                }
            })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registrationViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.logInFragment, false)
        }
    }
}