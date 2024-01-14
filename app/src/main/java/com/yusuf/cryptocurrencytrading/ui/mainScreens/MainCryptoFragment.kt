package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentMainCryptoBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.CoinAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.ImageAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.MainCryptoViewModel
import dagger.hilt.android.AndroidEntryPoint

import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class MainCryptoFragment : Fragment() {

    private lateinit var binding: FragmentMainCryptoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var viewModel: MainCryptoViewModel
    private lateinit var coinList: List<CryptoCurrency>
    private lateinit var adapter : CoinAdapter

    private val imageResources = arrayOf(R.drawable.crypto_image1, R.drawable.crypto_image2, R.drawable.crypto_image3)
    private val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCryptoBinding.inflate(inflater,container, false)
        auth = Firebase.auth
        firestore = Firebase.firestore
        viewModel = ViewModelProvider(this).get(MainCryptoViewModel::class.java)

        viewModel.getAllCoins()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageAdapter = ImageAdapter(imageResources)
        binding.viewPager2Main.adapter = imageAdapter
        viewPager2Setup()

        getUserName()

        setupRecyclerView()
        observeCoins()



        binding.imageViewLogOut.setOnClickListener {
            logOut()
        }


    }

    fun getUserName() {
        viewModel.getUserName(
            onSuccess = { username ->
                binding.textViewUserName.text = username
            },
            onFailure = { errorMessage ->
                Log.e("MainCryptoFragment", errorMessage)
            }
        )
    }

    fun viewPager2Setup(){
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val currentItem = binding.viewPager2Main.currentItem
                binding.viewPager2Main.setCurrentItem((currentItem + 1) % imageResources.size, true)
            }
        }, 0, 10000)
    }

    fun logOut(){
            viewModel.logOut {
                val action = MainCryptoFragmentDirections.actionMainCryptoFragmentToSignIn()
                findNavController().navigate(action)
            }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
    }

    fun observeCoins(){
        viewModel.coins.observe(viewLifecycleOwner){
            coins->
            coins?.let {
                adapter = CoinAdapter(requireContext(),coins.data.cryptoCurrencyList)
                binding.recyclerView.adapter=adapter
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllCoins()
    }

}



//binding.button2.setOnClickListener {

/*val list = arrayListOf("as","assa","sasa","asas")
val userRef = firestore.collection("users").document(auth.currentUser!!.uid)

userRef
    .update("fullName", list)
    .addOnSuccessListener {
        Toast.makeText(requireContext(),"Kaydedildi",Toast.LENGTH_LONG).show()
    }
    .addOnFailureListener {
        Toast.makeText(requireContext(),it.localizedMessage ?: "An unknown error occurred.",Toast.LENGTH_LONG).show()
    }*/