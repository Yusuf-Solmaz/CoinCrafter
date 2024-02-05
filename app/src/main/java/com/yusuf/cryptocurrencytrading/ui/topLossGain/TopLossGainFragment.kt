package com.yusuf.cryptocurrencytrading.ui.topLossGain

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.cryptocurrencytrading.databinding.FragmentTopLossGainBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.MarketAdapter
import com.yusuf.cryptocurrencytrading.ui.topLossGain.viewModel.TopLossGainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopLossGainFragment : Fragment() {

    private lateinit var adapter: MarketAdapter
    private lateinit var viewModel: TopLossGainViewModel
    private lateinit var binding: FragmentTopLossGainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopLossGainBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this).get(TopLossGainViewModel::class.java)

        setupRecyclerView()

        getData()

        return binding.root
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.topLossGainRecyclerView.layoutManager = layoutManager
    }

    private fun getData() {
        val position = requireArguments().getInt("position")
        Log.i("position", position.toString())

        viewModel.coins.observe(viewLifecycleOwner) { cryptoCurrencyList ->
            cryptoCurrencyList?.let {
                val list = ArrayList(cryptoCurrencyList)
                binding.topLossGainRecyclerView.adapter = MarketAdapter(requireContext(), list)
            }
        }

        viewModel.getAllCoins(position)
    }
}
