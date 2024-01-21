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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentMarketBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.MarketAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.MarketViewModel
import com.yusuf.cryptocurrencytrading.ui.topLossGain.adapter.TopLossGainAdapter
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding
    private lateinit var viewModel: MarketViewModel
    private lateinit var adapter: MarketAdapter
    private lateinit var searchText: String

    private lateinit var list: List<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(layoutInflater, container, false)

        setTabLayout()
        list = listOf()

        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        viewModel.getAllCoins()


        adapter = MarketAdapter(requireContext(), list)
        binding.marketRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCoins()
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

    fun observeCoins() {
        viewModel.coins.observe(viewLifecycleOwner) { coins ->
            coins?.let {
                if (list.isEmpty()) {

                    list = coins.data.cryptoCurrencyList
                    adapter = MarketAdapter(requireContext(), list)
                    binding.marketRecyclerView.adapter = adapter
                }
            }
        }
    }

    private fun setTabLayout() {
        val adapter = TopLossGainAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0) {
                    binding.topGainIndicator.visible()
                    binding.topLoseIndicator.gone()
                } else {
                    binding.topGainIndicator.gone()
                    binding.topLoseIndicator.visible()
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager) { tab, position ->
            var title = if (position == 0) {
                "Top Gainers"
            } else {
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }
}
