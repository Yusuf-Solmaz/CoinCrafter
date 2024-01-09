package com.yusuf.cryptocurrencytrading.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentSignInBinding

class SignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToSingUp.setOnClickListener {
            val action = SignInDirections.actionSignInToSignUp()
            findNavController().navigate(action)
        }
    }
}