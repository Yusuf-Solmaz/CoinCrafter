package com.yusuf.cryptocurrencytrading.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignInBinding
import com.yusuf.cryptocurrencytrading.ui.registration.viewModel.RegistrationViewModel

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        checkUserIfNotNull()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToSingUp.setOnClickListener {
            val action = SignInDirections.actionSignInToSignUp()
            findNavController().navigate(action)
        }

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            signIn(email,password)
        }

    }

    private fun signIn(email:String,password:String){

        viewModel.signIn(email,password,requireContext(),{
            val action = SignInDirections.actionSignInToMainCryptoFragment()
            findNavController().navigate(action)
        },{
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun checkUserIfNotNull(){
        viewModel.checkUserIfNotNull {
            val action = SignInDirections.actionSignInToMainCryptoFragment()
            findNavController().navigate(action)
        }
    }
}