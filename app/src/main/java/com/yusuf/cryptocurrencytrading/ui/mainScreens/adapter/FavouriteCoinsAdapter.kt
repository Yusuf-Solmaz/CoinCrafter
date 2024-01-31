package com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.firebase.entity.FavouriteCryptosFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FavCoinsRowBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.FavouriteCoinsFragmentDirections

class FavouriteCoinsAdapter(val context: Context,var favCoins: List<FavouriteCryptosFirebase>,var coins: List<CryptoCurrency>): RecyclerView.Adapter<FavouriteCoinsAdapter.FavouriteCoinsHolder>() {
    class FavouriteCoinsHolder(val binding: FavCoinsRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCoinsHolder {
        return FavouriteCoinsHolder(FavCoinsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  favCoins.size
    }

    override fun onBindViewHolder(holder: FavouriteCoinsHolder, position: Int) {
        val favCoinHashMap = favCoins[position]


        Log.i("FavCoinnsAdapter",favCoinHashMap.toString())

        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)
        holder.binding.textViewCoinName.text = favCoinHashMap.name

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/${favCoinHashMap.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(holder.binding.imageViewCoinImage)


        holder.binding.cardView.setOnClickListener {
            for (data in coins){
                if (data.name != favCoinHashMap.name){
                    continue
                }
                else{
                    holder.binding.root.findNavController().navigate(FavouriteCoinsFragmentDirections.actionFavouriteCoinsFragmentToCoinDetailFragment(data))
                }
            }
        }

    }

    fun updateData(newFavCoins: List<FavouriteCryptosFirebase>, newCoins: List<CryptoCurrency>) {
        favCoins = newFavCoins
        coins = newCoins
        notifyDataSetChanged()
    }
}