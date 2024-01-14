package com.yusuf.cryptocurrencytrading.ui.registration


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignUpBinding
import com.yusuf.cryptocurrencytrading.ui.registration.viewModel.RegistrationViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        binding.goToSignIn.setOnClickListener {
            val action = SignUpDirections.actionSignUpToSignIn()
            findNavController().navigate(action)

        }

        binding.saveButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()
            val email = binding.editTextEmail.text.toString()

            viewModel.signUp(email, password, name, requireContext(),{
                val action = SignUpDirections.actionSignUpToSignIn()
                findNavController().navigate(action)

            }, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
            Log.i("info", "$name $password $email")
        }
    }

    private fun observeData(){
        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.progressBar.visible()
                binding.saveButton.isClickable= false
                binding.saveButton.setBackgroundResource(R.drawable.button_loading_color)
            }
            else{
                binding.progressBar.gone()
                binding.saveButton.isClickable= true
            }
        }
    }
}