package com.yusuf.cryptocurrencytrading.ui.registration

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignUpBinding
import com.yusuf.cryptocurrencytrading.ui.registration.viewModel.RegistrationViewModel

class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSignIn.setOnClickListener {
            val action = SignUpDirections.actionSignUpToSignIn()
            findNavController().navigate(action)


        }

        binding.saveButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()
            val email = binding.editTextEmail.text.toString()

            viewModel.signUp(email, password, name, {
                val action = SignUpDirections.actionSignUpToMainCryptoFragment()
                findNavController().navigate(action)

            }, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
            Log.i("info", "$name $password $email")
        }
    }
}