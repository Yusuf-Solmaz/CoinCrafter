package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentFavouriteCoinsBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.FavouriteCoinsAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.FavouriteCoinsViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class FavouriteCoinsFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteCoinsBinding
    private lateinit var viewModel: FavouriteCoinsViewModel
    private lateinit var adapter: FavouriteCoinsAdapter

    private lateinit var currentCoinList: List<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteCoinsBinding.inflate(layoutInflater,container,false)

        viewModel = ViewModelProvider(this).get(FavouriteCoinsViewModel::class.java)

        currentCoinList = listOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        getFavData()
    }


    private fun observedata() {

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.favRecyclerView.gone()
                binding.progressBarFavorites.visible()
            }
            else{
                binding.favRecyclerView.visible()
                binding.progressBarFavorites.gone()
            }
        }

        viewModel.favCoins.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                binding.noFavText.visible()
            }
            else{
                binding.noFavText.gone()
            }
        }

        viewModel.coins.observe(viewLifecycleOwner) { currentCoin ->
            Log.i("currentCoinListFav", currentCoin.data.cryptoCurrencyList[0].toString())
            currentCoinList = currentCoin.data.cryptoCurrencyList

            if (!::adapter.isInitialized) {
                adapter = FavouriteCoinsAdapter(requireContext(), viewModel.favCoins.value ?: emptyList(), currentCoinList)
                binding.favRecyclerView.adapter = adapter
            } else {
                adapter.updateData(viewModel.favCoins.value ?: emptyList(), currentCoinList)
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.favRecyclerView.layoutManager = layoutManager
    }

    private fun getFavData(){
        observedata()
        viewModel.getAllCoins()
        viewModel.getFavouriteCryptos()
    }

    override fun onResume() {
        super.onResume()
        getFavData()
    }

}