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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(layoutInflater, container, false)

        setTabLayout()

        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        viewModel.getAllCoins()

        return binding.root
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
