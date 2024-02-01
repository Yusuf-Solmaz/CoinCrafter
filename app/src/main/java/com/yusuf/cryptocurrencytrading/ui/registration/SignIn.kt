package com.yusuf.cryptocurrencytrading.ui.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yusuf.cryptocurrencytrading.MainActivity
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignInBinding
import com.yusuf.cryptocurrencytrading.ui.registration.viewModel.RegistrationViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: RegistrationViewModel
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        checkUserIfNotNull()
        observeData()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun observeData(){
        viewModel.loading.observe(viewLifecycleOwner){
            if (it!=null){
                if (it==true){
                    binding.progressBar2.visible()
                    binding.buttonSignIn.isClickable=false
                    binding.buttonSignIn.setBackgroundResource(R.drawable.button_loading_color)
                }
                else{
                    binding.progressBar2.gone()
                    binding.buttonSignIn.isClickable= true
                }
            }
        }

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
            if (mContext is MainActivity) {
                (mContext as MainActivity).goToCoinActivity()
            }
        },{
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun checkUserIfNotNull(){
        viewModel.checkUserIfNotNull {
            if (mContext is MainActivity) {
                (mContext as MainActivity).goToCoinActivity()
            }
        }
    }
}