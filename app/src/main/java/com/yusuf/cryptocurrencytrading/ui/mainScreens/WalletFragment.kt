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
import com.yusuf.cryptocurrencytrading.databinding.FragmentWalletBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter.WalletUserCryptoAdapter
import com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog.AddBalanceDialog
import com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog.CheckBalanceDialog
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.WalletViewModel
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible
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
        getData()

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

        viewModel.loadingCoins.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerViewMyCrypto.gone()
                binding.progressBarUserCoins.visible()
            }
            else{
                binding.recyclerViewMyCrypto.visible()
                binding.progressBarUserCoins.gone()
            }
        }

        viewModel.loadingBalance.observe(viewLifecycleOwner){
            if (it){
                binding.totalBalance.gone()
                binding.progressBarUserBalance.visible()
            }
            else{
                binding.totalBalance.visible()
                binding.progressBarUserBalance.gone()
            }
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            Log.i("balanceData",it.toString())
            binding.totalBalance.text = "${String.format("%.02f", it)}$"
        }

        viewModel.coins.observe(viewLifecycleOwner){
            currentCoin ->

            Log.i("currentCoinList",currentCoin.data.cryptoCurrencyList[0].toString())
            currentCoinList = currentCoin.data.cryptoCurrencyList

        }

        viewModel.userCoinList.observe(viewLifecycleOwner){

            Log.i("userCoinListWalletFragment",it.toString())
            adapter = WalletUserCryptoAdapter(viewModel,requireContext(),it,currentCoinList)
            binding.recyclerViewMyCrypto.adapter = adapter



            if (it.isEmpty()){
                binding.textView6.gone()
                binding.textView8.gone()
                binding.textView10.gone()
                Log.i("currentCoinListNull",currentCoinList.toString())
            }
            else{
                binding.textView6.visible()
                binding.textView8.visible()
                binding.textView10.visible()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData(){
        observeData()
        viewModel.getAllCoins()
        viewModel.getUserCoins()
        viewModel.getBalance()
    }

    private fun showAddBalanceDialog() {
        val dialog = AddBalanceDialog()

        dialog.setOnDismissListener{
            binding.recyclerViewMyCrypto.visible()
        }

        dialog.setTargetFragment(this, 0)
        dialog.show(requireFragmentManager(), "AddBalanceDialog")
    }

    private fun showCheckBalanceDialog(){
        val dialog = CheckBalanceDialog()

        dialog.setOnDismissListener{
            binding.recyclerViewMyCrypto.visible()
        }

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