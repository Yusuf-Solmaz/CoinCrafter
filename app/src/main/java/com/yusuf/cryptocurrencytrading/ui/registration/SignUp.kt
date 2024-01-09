package com.yusuf.cryptocurrencytrading.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignUpBinding

class SignUp : Fragment() {

    private lateinit var binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSignIn.setOnClickListener {
            val action =  SignUpDirections.actionSignUpToSignIn()
            findNavController().navigate(action)
        }
    }

}