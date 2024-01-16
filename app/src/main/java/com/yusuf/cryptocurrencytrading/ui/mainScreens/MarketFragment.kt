package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.databinding.FragmentMarketBinding
import com.yusuf.cryptocurrencytrading.ui.topLossGain.adapter.TopLossGainAdapter
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketBinding.inflate(layoutInflater,container,false)

        setTabLayout()
        return binding.root
    }

    private fun setTabLayout() {
        val adapter = TopLossGainAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0){
                    binding.topGainIndicator.visible()
                    binding.topLoseIndicator.gone()
                }
                else{
                    binding.topGainIndicator.gone()
                    binding.topLoseIndicator.visible()
                }
            }
        })

        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab, position ->
            var title = if (position ==0){
                "Top Gainers"
            }
            else{
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }

}