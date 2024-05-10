package com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter

import SellCryptoDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.firebase.entity.CryptoFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.UserCryptoRowBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.WalletFragmentDirections
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.WalletViewModel

class WalletUserCryptoAdapter(val viewModel: WalletViewModel,val context: Context,var userCoins: List<CryptoFirebase>,var coins:List<CryptoCurrency>): RecyclerView.Adapter<WalletUserCryptoAdapter.UserCryptoHolder>() {

    class UserCryptoHolder(val binding: UserCryptoRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    init {
        Log.i("userCoinsSize", userCoins.size.toString())
        Log.i("WalletAdapterInit", userCoins.toString())
        Log.i("WalletAdapterInitCoin", coins.size.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCryptoHolder {
        return UserCryptoHolder(
            UserCryptoRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return userCoins.size
    }

    override fun onBindViewHolder(holder: UserCryptoHolder, position: Int) {
        Log.i("WalletAdapter", coins[position].toString())
        val coinHashMap = userCoins[position] as HashMap<*, *>


        val amount = coinHashMap["amount"] as Double
        val price = coinHashMap["price"] as Double
        val name = coinHashMap["name"] as String
        val id = (coinHashMap["id"] as Long).toInt()

        val coin = CryptoFirebase(amount, price, name, id)
        Log.i("WalletAdapterCoin", userCoins.toString())

        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)
        holder.binding.coinName.text = coin.name


        holder.binding.coinAmount.text = String.format("%.5f", coin.amount)

        for (data in coins) {
            if (coin.name != data.name) {
                continue
            } else {
                val currentCoinPrice = data.quotes[0].price
                val totalPrice = coin.amount * currentCoinPrice
                val boughtCoinPrice = coin.amount * coin.price

                val profit = totalPrice - boughtCoinPrice

                if (profit > 0) {
                    holder.binding.coinProfit.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green
                        )
                    )
                    holder.binding.coinProfit.text = "${String.format("%.02f", profit)}$"
                } else if (profit < 0) {
                    holder.binding.coinProfit.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                    holder.binding.coinProfit.text = "${String.format("%.02f", profit)}$"
                } else {
                    holder.binding.coinProfit.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grey
                        )
                    )
                    holder.binding.coinProfit.text = "${String.format("%.02f", profit)}$"

                }
            }
        }

        holder.binding.buttonSell.setOnClickListener {

            val sellDialog = SellCryptoDialog(context)
            sellDialog.setOnSellClickListener(object : SellCryptoDialog.OnSellClickListener {
                override fun onSellClick(sellAmount: Double) {

                    viewModel.sellCrypto(coin.name, sellAmount,it,coins)
                }
            },object : SellCryptoDialog.OnSellAllClickListener{
                override fun onSellAllClick() {

                    viewModel.sellAllSelectedCrypto(coin.name,it,coins)
                }
            })
            sellDialog.show()
        }

        holder.binding.cardView.setOnClickListener {
            var coinTransferable: CryptoCurrency? = null

            for (data in coins){
                if (coin.name != data.name){
                    continue
                }
                else{
                    coinTransferable = data
                }
            }
            if (coinTransferable !=null) {
                holder.binding.root.findNavController().navigate(
                    WalletFragmentDirections.actionWalletFragmentToCoinDetailFragment(
                        coinTransferable
                    )
                )
            }
            else{
                Log.i("coinTransferable", coinTransferable.toString())
            }
        }
    }

}