package com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.CoinListCardviewBinding
import com.yusuf.cryptocurrencytrading.ui.mainScreens.MainCryptoFragmentDirections

class CoinAdapter(val context: Context,val coinList: List<CryptoCurrency>) : RecyclerView.Adapter<CoinAdapter.CoinHolder>() {

    class CoinHolder(val binding: CoinListCardviewBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinHolder {
        val itemBinding = CoinListCardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CoinHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return coinList.size
    }

    override fun onBindViewHolder(holder: CoinHolder, position: Int) {

        val coin = coinList[position]


        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)

        holder.binding.cardView.setOnClickListener {
            Log.i("coinMainName",coin.name)

            holder.binding.root.findNavController().navigate(
                MainCryptoFragmentDirections.actionMainCryptoFragment2ToCoinDetailFragment(coin)
            )

        }


        holder.binding.coinName.text = coin.name

        holder.binding.coinValue.text ="${String.format("%.2f",coin.quotes[0].price)}$"

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(holder.binding.coinImg)

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png"
        ).placeholder(R.drawable.graph).into(holder.binding.coinGraphImg)


        if (0 > coin.quotes[0].percentChange24h){
            holder.binding.coinRatio.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        } else {
            holder.binding.coinRatio.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        }
    }
}