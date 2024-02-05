package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentAllCoinsBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.AllCoinsAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.AllCoinsViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AllCoinsFragment : Fragment() {

    private lateinit var binding: FragmentAllCoinsBinding
    private lateinit var viewModel: AllCoinsViewModel
    private lateinit var adapter: AllCoinsAdapter
    private lateinit var list: List<CryptoCurrency>
    private lateinit var searchText: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCoinsBinding.inflate(layoutInflater,container,false)

        list = listOf()
        viewModel = ViewModelProvider(this).get(AllCoinsViewModel::class.java)
        viewModel.getAllCoins()


        adapter = AllCoinsAdapter(requireContext(), list)
        binding.marketRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("submit", query.toString())

                searchText = query.toString().toLowerCase()
                updateRecyclerView(searchText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText.toString().toLowerCase()
                updateRecyclerView(searchText)
                return true
            }
        })
    }

    private fun updateRecyclerView(searchText: String) {
        Log.i("updateRecyclerView", "Search Text: $searchText")

        val data = ArrayList<CryptoCurrency>()

        for (item in list) {
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)) {
                Log.i("updateRecyclerViewListItem", item.name.toString())
                data.add(item)
            }
        }

        adapter.updateData(data)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.marketRecyclerView.layoutManager = layoutManager
    }

    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.searchView.gone()
                binding.marketRecyclerView.gone()
                binding.progressBarAllCoins.visible()
            }
            else{
                binding.progressBarAllCoins.gone()
                binding.marketRecyclerView.visible()
                binding.searchView.visible()
            }
        }

        viewModel.coins.observe(viewLifecycleOwner) { coins ->
            coins?.let {
                if (list.isEmpty()) {

                    list = coins.data.cryptoCurrencyList
                    adapter = AllCoinsAdapter(requireContext(), list)
                    binding.marketRecyclerView.adapter = adapter
                }
            }
        }
    }



}