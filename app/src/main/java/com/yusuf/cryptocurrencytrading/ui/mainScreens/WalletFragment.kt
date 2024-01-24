package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentWalletBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.WalletUserCryptoAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog.AddBalanceDialog
import com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog.CheckBalanceDialog
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.MarketViewModel
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var viewModel: WalletViewModel
    private lateinit var adapter: WalletUserCryptoAdapter

    private lateinit var currentCoinList: List<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWalletBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        observeData()

        binding.deposit.setOnClickListener {
            showAddBalanceDialog()
        }

        binding.withdraw.setOnClickListener {
            showCheckBalanceDialog()
        }

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMyCrypto.layoutManager = layoutManager
    }

    fun observeData(){
        viewModel.balance.observe(viewLifecycleOwner) {
            Log.i("balanceData",it.toString())
            binding.totalBalance.text = "$it$"
        }

        viewModel.coins.observe(viewLifecycleOwner){
            currentCoin ->
            Log.i("currentCoinList",currentCoin.data.cryptoCurrencyList[0].toString())
            currentCoinList = currentCoin.data.cryptoCurrencyList
        }

        viewModel.userCoinList.observe(viewLifecycleOwner){

            Log.i("userCoinListWalletFragment",it.toString())
            adapter = WalletUserCryptoAdapter(requireContext(),it,currentCoinList)
            binding.recyclerViewMyCrypto.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()
        viewModel.getAllCoins()
        viewModel.getUserCoins()
        viewModel.getBalance()
    }

    private fun showAddBalanceDialog() {
        val dialog = AddBalanceDialog()
        dialog.setTargetFragment(this, 0)
        dialog.show(requireFragmentManager(), "AddBalanceDialog")
    }

    private fun showCheckBalanceDialog(){
        val dialog = CheckBalanceDialog()
        dialog.setTargetFragment(this, 0)
        dialog.show(requireFragmentManager(), "CheckBalanceDialog")
    }


    fun addBalance(amount: Double) {
        viewModel.addToBalance(amount,requireView())
    }

    fun checkBalance(amount: Double) {
        viewModel.checkBalance(amount,requireView())
    }


}