package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.yusuf.cryptocurrencytrading.CoinActivity
import com.yusuf.cryptocurrencytrading.MainActivity
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentMainCryptoBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.CoinAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.ImageAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.MainCryptoViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.invisible
import com.yusuf.cryptocurrencytrading.utils.visible
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
    private var mContext: Context? = null

    private val imageResources = arrayOf(R.drawable.p1, R.drawable.p2, R.drawable.p3)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageAdapter = ImageAdapter(imageResources)
        binding.viewPager2Main.adapter = imageAdapter
        viewPager2Setup()


        observeData()
        getUserName()

        getData()
        setupRecyclerView()





        binding.imageViewLogOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
            .setTitle("Log Out")
            .setMessage("Are You Sure You Want to Log Out?")


            .setPositiveButton("Yes") { dialog, which ->
                logOut()
                dialog.dismiss()
            }


            .setNegativeButton("No") { dialog, which ->

                dialog.dismiss()
            }
                .create().show()
        }

        binding.textViewViewAll.setOnClickListener{
            findNavController().navigate(MainCryptoFragmentDirections.actionMainCryptoFragment2ToAllCoinsFragment())
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
                if (mContext is CoinActivity) {
                    (mContext as CoinActivity).goToMainActivity()
                }
            }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
    }

    fun getData(){
        viewModel.getAllCoins()
    }

    fun observeData(){

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerView.invisible()
                binding.progressBarMainCrypto.visible()
            }
            else{
                binding.progressBarMainCrypto.gone()
                binding.recyclerView.visible()
            }
        }

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
        observeData()
        getUserName()

        getData()
    }

}